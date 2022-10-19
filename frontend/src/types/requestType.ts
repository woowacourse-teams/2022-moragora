import { ErrorResponseBody } from './userType';

export type SuccessInterceptHandler = (res: Response) => void;
export type ErrorInterceptHandler = (
  res: Response,
  body: ErrorResponseBody
) => void;

export type Interceptor = {
  onSuccess: SuccessInterceptHandler;
  onError: ErrorInterceptHandler;
  set: (
    onSuccess?: SuccessInterceptHandler,
    onError?: ErrorInterceptHandler
  ) => void;
};
