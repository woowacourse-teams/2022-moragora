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
import { getLoginUserDataApi } from 'apis/userApis';
import useInterceptor from 'hooks/useInterceptor';

const App = () => {
  const { login, logout, accessToken } = useContext(
    userContext
  ) as UserContextValues;

  useInterceptor({
    onError: (response) => {
      switch (response.status) {
        case 401: {
          logout();
        }
      }
    },
  });

  const { isLoading } = useQuery(
    ['loginUserData'],
    getLoginUserDataApi(accessToken),
    {
      enabled: !!accessToken,
      onSuccess: ({ body }) => {
        if (accessToken) {
          login(body, accessToken);
        }
      },
    }
  );

  if (isLoading) {
    return (
      <AppLayout>
        <S.SpinnerBox>
          <Spinner />
        </S.SpinnerBox>
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
