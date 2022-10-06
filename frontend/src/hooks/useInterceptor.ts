import { useEffect } from 'react';
import { interceptor, type InterceptHandler } from 'utils/request';

type UseInterceptor = {
  onSuccess: InterceptHandler;
  onError: InterceptHandler;
};

const useInterceptor = ({ onSuccess, onError }: Partial<UseInterceptor>) => {
  useEffect(() => {
    interceptor.setInterceptor(onSuccess, onError);
  }, []);
};

export default useInterceptor;
