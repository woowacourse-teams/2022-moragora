import { useContext } from 'react';
import Router from 'router';
import * as S from './App.styled';
import Header from 'components/layouts/Header';
import Spinner from 'components/@shared/Spinner';
import { userContext, UserContextValues } from 'contexts/userContext';
import { CalendarProvider } from 'contexts/calendarContext';
import useQuery from 'hooks/useQuery';
import { getLoginUserDataApi } from 'apis/userApis';
import AppLayout from 'components/layouts/AppLayout';

const App = () => {
  const { login, accessToken } = useContext(userContext) as UserContextValues;
  const { isLoading } = useQuery(
    ['loginUserData'],
    getLoginUserDataApi(accessToken),
    {
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
          <Router />
        </CalendarProvider>
      </AppLayout>
      <div id="root-modal" />
    </>
  );
};

export default App;
