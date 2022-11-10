import useQuery from 'hooks/useQuery';

export type Variables = Record<string, any> | null;

export type QueryStatus = 'idle' | 'loading' | 'error' | 'success';

export type QueryClient = {
  dataCache: Record<string, any>;
  queryCache: (() => Promise<void>) | null;
  invalidateQueries: (key: string) => void;
  isCached: (key: string) => boolean;
  clearQueryCache: () => void;
  reQueryCache: () => void;
  setQueryCache: (query: () => Promise<void>) => void;
};

export type QueryOptions<TData> = {
  onSuccess: (data: TData) => Promise<void> | void;
  onError: (error: Error) => Promise<void> | void;
  onSettled: (
    data: TData | undefined,
    error: Error | undefined
  ) => Promise<void> | void;
  enabled: boolean;
  refetchOnMount: boolean;
  refetchOnWindowFocus: boolean;
  refetchInterval: number;
  retry: number;
  cacheTime: number;
};

export type MutationOptions<TData, TVariables> = {
  onSuccess: (data: TData, variables: TVariables) => Promise<void> | void;
  onError: (error: Error, variables: TVariables) => Promise<void> | void;
  onSettled: (
    data: TData | undefined,
    error: Error | undefined,
    variables: TVariables
  ) => Promise<void> | void;
  onMutate: (variables: TVariables) => Promise<void> | void;
  retry: number;
};

export type QueryState<T> = ReturnType<
  typeof useQuery<Awaited<ReturnType<ReturnType<T>>>>
>;
