import { useContext } from 'react';
import Router from 'router';
import * as S from './App.styled';
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

const App = () => {
  const userState = useContext(userContext) as UserContextValues;

  useInterceptor({
    onError: (response) => {
      switch (response.status) {
        case 401: {
          userState?.logout();
          break;
        }
      }
    },
  });

  const refreshQuery = useQuery(['refresh'], accessTokenRefreshApi, {
    onSuccess: () => {},
  });

  const { isLoading } = useQuery(
    ['loginUserData'],
    getLoginUserDataApi(userState.accessToken),
    {
      enabled: !!userState.accessToken,
      onSuccess: ({ body }) => {
        if (userState.accessToken) {
          userState.login(body, userState?.accessToken);
        }
      },
      onError: (error) => {
        const statusCode = error.message.split(': ')[0];

        if (statusCode === '404') {
          userState?.logout();
        }
      },
    }
  );

  if (isLoading) {
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
