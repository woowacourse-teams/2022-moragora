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
import { TokenStatus } from 'types/userType';
import { queryClient } from 'contexts/queryClient';
import * as S from './App.styled';

const App = () => {
  const userState = useContext(userContext) as UserContextValues;

  useInterceptor({
    accessToken: userState.accessToken,
    onRequest: (url, options, accessToken) => ({
      url,
      options: {
        ...options,
        headers: {
          ...options.headers,
          Authorization: `Bearer ${accessToken}`,
        },
      },
    }),
    onError: (response, body) => {
      if (response.status !== 401) {
        return;
      }

      if (body.tokenStatus === TokenStatus['invalid']) {
        userState.logout();
        return;
      }

      if (body.tokenStatus === TokenStatus['expired']) {
        refreshQuery.refetch();
        return;
      }

      userState.setInitialized(true);
    },
  });

  const refreshQuery = useQuery(['refresh'], accessTokenRefreshApi, {
    onSuccess: ({ body: { accessToken } }) => {
      userState.updateAccessToken(accessToken);
      userState.setInitialized(true);
      queryClient.reQueryCache();
    },
  });

  const getLoginUserDataQuery = useQuery(
    ['loginUserData'],
    getLoginUserDataApi(),
    {
      enabled: !!userState.accessToken,
      onSuccess: ({ body }) => {
        if (userState.accessToken) {
          userState.login(body, userState.accessToken);
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
