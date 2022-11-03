import { useCallback, useEffect } from 'react';
import { AxiosRequestConfig } from 'axios';
import { publicRequest, privateRequest } from 'apis/instances';
import { TokenStatus } from 'types/userType';
import { UserContextValues } from 'contexts/userContext';

type useInterceptorProps = {
  userState: UserContextValues;
};

const useInterceptor = (customConfig: useInterceptorProps) => {
  const { userState } = customConfig;

  const onRequest = useCallback(
    (config: AxiosRequestConfig<any>) => {
      return {
        ...config,
        headers: {
          ...config.headers,
          Authorization: `Bearer ${userState.accessToken}`,
        },
      };
    },
    [customConfig]
  );

  const onRejected = useCallback(
    async (error: any) => {
      const {
        config,
        response: {
          status,
          data: { tokenStatus },
        },
      } = error;

      if (status !== 401) {
        return Promise.reject(error);
      }

      if (tokenStatus === TokenStatus['invalid']) {
        userState.logout();
        return;
      }

      if (tokenStatus === TokenStatus['expired']) {
        const {
          data: { accessToken },
        } = await publicRequest('/token/refresh');

        const newConfig = {
          ...config,
          Authorization: `Bearer ${accessToken}`,
        };

        userState.setAccessToken(accessToken);

        return privateRequest(newConfig);
      }

      userState.setInitialized(true);
      return Promise.reject(error);
    },
    [customConfig]
  );

  useEffect(() => {
    publicRequest.interceptors.response.clear();
    publicRequest.interceptors.response.use((config) => config, onRejected);

    privateRequest.interceptors.request.clear();
    privateRequest.interceptors.response.clear();
    privateRequest.interceptors.request.use(onRequest);
    privateRequest.interceptors.response.use((config) => config, onRejected);
  }, [onRejected, onRequest]);
};

export default useInterceptor;
