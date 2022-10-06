import { useEffect } from 'react';
import { interceptor } from 'utils/request';
import type { InterceptHandler } from 'types/requestType';

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
