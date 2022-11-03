import { useEffect } from 'react';
import { AxiosRequestConfig, AxiosResponse } from 'axios';
import { publicRequest, privateRequest } from 'apis/instances';

type UseInterceptorProps = {
  onRequest: (
    config: AxiosRequestConfig<unknown>
  ) => AxiosRequestConfig<unknown>;
  onRejected: (error: unknown) => Promise<AxiosResponse<any, any> | undefined>;
  resolver: unknown[];
};

const useInterceptor = ({
  onRejected,
  onRequest,
  resolver,
}: UseInterceptorProps) => {
  useEffect(() => {
    publicRequest.interceptors.response.clear();
    publicRequest.interceptors.response.use((config) => config, onRejected);

    privateRequest.interceptors.request.clear();
    privateRequest.interceptors.response.clear();
    privateRequest.interceptors.request.use(onRequest);
    privateRequest.interceptors.response.use((config) => config, onRejected);
  }, [...resolver]);
};

export default useInterceptor;
