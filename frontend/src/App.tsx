import { useContext } from 'react';
import Router from 'router';
import * as S from './App.styled';
import Header from 'components/layouts/Header';
import Spinner from 'components/@shared/Spinner';
import { userContext, UserContextValues } from 'contexts/userContext';
import { CalendarProvider } from 'contexts/calendarContext';
import useQuery from 'hooks/useQuery';
import { getLoginUserDataApi } from 'apis/userApis';
import DeviceLayout from 'components/layouts/DeviceLayout';

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
      <DeviceLayout>
        <S.SpinnerBox>
          <Spinner />
        </S.SpinnerBox>
      </DeviceLayout>
    );
  }

  return (
    <>
      <DeviceLayout>
        <Header />
        <CalendarProvider initialDate={new Date()}>
          <Router />
        </CalendarProvider>
      </DeviceLayout>
      <div id="root-modal" />
    </>
  );
};

export default App;
