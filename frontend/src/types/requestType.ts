import { ErrorResponseBody } from './userType';

export type RequestInterceptHandler = (
  url: string,
  options: RequestInit,
  accessToken: string | null
) => {
  url: string;
  options: RequestInit;
};
export type SuccessInterceptHandler = (res: Response) => void;
export type ErrorInterceptHandler = (
  res: Response,
  body: ErrorResponseBody
) => void;

export type Interceptor = {
  accessToken: string | null;
  onRequest: RequestInterceptHandler;
  onSuccess: SuccessInterceptHandler;
  onError: ErrorInterceptHandler;
  set: (handler: {
    onRequest?: RequestInterceptHandler;
    onSuccess?: SuccessInterceptHandler;
    onError?: ErrorInterceptHandler;
  }) => void;
};
