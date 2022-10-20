import { useEffect } from 'react';
import { interceptor } from 'utils/request';
import type {
  ErrorInterceptHandler,
  RequestInterceptHandler,
  SuccessInterceptHandler,
} from 'types/requestType';

type UseInterceptor = {
  accessToken: string | null;
  onRequest: RequestInterceptHandler;
  onSuccess: SuccessInterceptHandler;
  onError: ErrorInterceptHandler;
};

const useInterceptor = ({
  accessToken,
  onRequest,
  onSuccess,
  onError,
}: Partial<UseInterceptor>) => {
  useEffect(() => {
    console.log(accessToken);
    if (accessToken !== undefined) {
      interceptor.accessToken = accessToken;
    }

    interceptor.set({ onRequest, onSuccess, onError });
  }, [accessToken, onRequest, onSuccess, onError]);
};

export default useInterceptor;
