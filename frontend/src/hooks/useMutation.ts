import { useState, useRef } from 'react';
import { MUTATION_STATUS } from 'consts';
import { Variables, MutationOptions } from 'types/queryType';

const useMutation = <TData = any, TVariables extends Variables = Variables>(
  mutationFn: (variables: TVariables) => Promise<TData>,
  {
    onSuccess,
    onSettled,
    onError,
    onMutate,
    retry = 0,
  }: Partial<MutationOptions<TData, TVariables>> = {}
) => {
  const [data, setData] = useState<TData>();
  const [error, setError] = useState<Error>();
  const [status, setStatus] = useState(MUTATION_STATUS.IDLE);
  const retryCountRef = useRef<number>(0);
  const isIdle = status === MUTATION_STATUS.IDLE;
  const isLoading = status === MUTATION_STATUS.LOADING;
  const isError = status === MUTATION_STATUS.ERROR;
  const isSuccess = status === MUTATION_STATUS.SUCCESS;

  const mutate = async (variables: TVariables) => {
    if (isLoading) {
      return;
    }

    try {
      setStatus(MUTATION_STATUS.LOADING);
      await onMutate?.(variables);

      const data = await mutationFn(variables);

      setData(data);
      setStatus(MUTATION_STATUS.SUCCESS);
      await onSuccess?.(data, variables);
    } catch (error) {
      setStatus(MUTATION_STATUS.ERROR);

      if (!(error instanceof Error)) {
        return;
      }

      setError(error);
      await onError?.(error, variables);

      if (retryCountRef.current < retry) {
        retryCountRef.current += 1;
        await mutate(variables);
      }
    } finally {
      await onSettled?.(data, error, variables);
    }
  };

  return { data, error, status, isIdle, isLoading, isError, isSuccess, mutate };
};

export default useMutation;
