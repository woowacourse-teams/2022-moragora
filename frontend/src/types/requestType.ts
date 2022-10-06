export type InterceptHandler = (response: Response) => void;

export type Interceptor = {
  onSuccess: InterceptHandler;
  onError: InterceptHandler;
  setInterceptor: (
    onSuccess?: InterceptHandler,
    onError?: InterceptHandler
  ) => void;
};
