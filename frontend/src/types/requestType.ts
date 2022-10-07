export type InterceptHandler = (response: Response) => void;

export type Interceptor = {
  onSuccess: InterceptHandler;
  onError: InterceptHandler;
  set: (onSuccess?: InterceptHandler, onError?: InterceptHandler) => void;
};
