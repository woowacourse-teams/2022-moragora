import { useContext } from 'react';
import Router from 'router';
import AppLayout from 'components/layouts/AppLayout';
import Header from 'components/layouts/Header';
import Footer from 'components/layouts/Footer';
import Body from 'components/layouts/Body';
import Spinner from 'components/@shared/Spinner';
import { userContext, UserContextValues } from 'contexts/userContext';
import { CalendarProvider } from 'contexts/calendarContext';
import useQuery from 'hooks/useQuery';
import { getLoginUserDataApi, accessTokenRefreshApi } from 'apis/userApis';
import useInterceptor from 'hooks/useInterceptor';
import { AxiosRequestConfig } from 'axios';
import { TokenStatus } from 'types/userType';
import { privateRequest } from 'apis/instances';
import * as S from './App.styled';

const App = () => {
  const userState = useContext(userContext) as UserContextValues;

  useInterceptor({
    onRequest: (config: AxiosRequestConfig<unknown>) => ({
      ...config,
      headers: {
        ...config.headers,
        Authorization: `Bearer ${userState.accessToken}`,
      },
    }),
    onRejected: async (error: any) => {
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
        } = await accessTokenRefreshApi();

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
    resolver: [userState.accessToken],
  });

  useQuery(['refresh'], accessTokenRefreshApi, {
    onSuccess: ({ data: { accessToken } }) => {
      userState.setAccessToken(accessToken);
    },
    onSettled: () => {
      userState.setInitialized(true);
    },
  });

  const getLoginUserDataQuery = useQuery(
    ['loginUserData'],
    getLoginUserDataApi(),
    {
      enabled: !!userState.accessToken,
      onSuccess: ({ data }) => {
        if (userState.accessToken) {
          userState.login(data, userState.accessToken);
        }
      },
      onError: (error) => {
        const statusCode = error.message.split(': ')[0];

        if (statusCode === '404') {
          userState.logout();
        }
      },
    }
  );

  if (getLoginUserDataQuery.isLoading || !userState.initialized) {
    return (
      <AppLayout>
        <Body>
          <S.SpinnerBox>
            <Spinner />
          </S.SpinnerBox>
        </Body>
      </AppLayout>
    );
  }

  return (
    <>
      <AppLayout>
        <Header />
        <CalendarProvider initialDate={new Date()}>
          <Body>
            <Router />
          </Body>
        </CalendarProvider>
        <Footer />
      </AppLayout>
      <div id="root-modal" />
    </>
  );
};

export default App;
