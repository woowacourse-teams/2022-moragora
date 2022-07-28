import { useEffect, useState, useRef, useCallback } from 'react';
import { useQueryClient } from 'contexts/queryClient';
import { QUERY_STATUS } from 'consts';
import { DefaultData, QueryOptions } from 'types/useQueryType';

const useQuery = <TData extends DefaultData = DefaultData>(
  key: string[],
  queryFn: () => Promise<TData>,
  {
    onSuccess,
    onError,
    onSettled,
    enabled = true,
    refetchOnMount = true,
    refetchOnWindowFocus,
    refetchInterval,
    retry = 0,
    cacheTime = 0,
  }: Partial<QueryOptions<TData>> = {}
) => {
  const queryClient = useQueryClient();
  const [data, setData] = useState<TData>();
  const [error, setError] = useState<Error>();
  const [status, setStatus] = useState(QUERY_STATUS.LOADING);
  const retryCountRef = useRef<number>(0);
  const intervalId = useRef<NodeJS.Timer>();
  const staleTimerId = useRef<NodeJS.Timer>();
  const joinedKey = key.join('.');
  const isLoading = status === QUERY_STATUS.LOADING;
  const isError = status === QUERY_STATUS.ERROR;
  const isSuccess = status === QUERY_STATUS.SUCCESS;

  const cacheData = (data: TData) => {
    queryClient.cache[joinedKey] = data;
    staleTimerId.current = setTimeout(() => {
      queryClient.invalidateQueries(joinedKey);
    }, cacheTime);
  };

  const queryData = async (): Promise<TData> => {
    let data;

    if (queryClient.isCached(joinedKey)) {
      data = queryClient.cache[joinedKey];
    } else {
      data = await queryFn();

      cacheData(data);
    }

    return data;
  };

  const refetch = async () => {
    try {
      setStatus(QUERY_STATUS.LOADING);

      const data = await queryData();

      setData(data);
      setStatus(QUERY_STATUS.SUCCESS);
      await onSuccess?.(data);
    } catch (error) {
      setStatus(QUERY_STATUS.ERROR);

      if (!(error instanceof Error)) {
        return;
      }

      setError(error);
      await onError?.(error);

      const shouldRetry = retryCountRef.current < retry;

      if (shouldRetry) {
        retryCountRef.current += 1;
        await refetch();
      }
    } finally {
      await onSettled?.(data, error);
    }
  };

  const handleFocus = useCallback(() => {
    if (refetchOnWindowFocus) {
      refetch();
    }
  }, [refetchOnWindowFocus]);

  useEffect(() => {
    window.addEventListener('focus', handleFocus);

    if (refetchInterval) {
      intervalId.current = setInterval(() => {
        refetch();
      }, refetchInterval);
    }

    return () => {
      window.removeEventListener('focus', handleFocus);

      if (intervalId.current) {
        clearInterval(intervalId.current);
      }
    };
  }, []);

  useEffect(() => {
    if (enabled && refetchOnMount) {
      refetch();
    }
  }, [enabled]);

  return { data, error, status, isLoading, isError, isSuccess };
};

export default useQuery;
