import { useEffect } from 'react';
import { interceptor } from 'utils/request';
import type {
  ErrorInterceptHandler,
  SuccessInterceptHandler,
} from 'types/requestType';

type UseInterceptor = {
  onSuccess: SuccessInterceptHandler;
  onError: ErrorInterceptHandler;
};

const useInterceptor = ({ onSuccess, onError }: Partial<UseInterceptor>) => {
  useEffect(() => {
    interceptor.set(onSuccess, onError);
  }, []);
};

export default useInterceptor;
