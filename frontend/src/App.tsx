import Router from 'router';
import { css } from '@emotion/react';
import MobileLayout from 'components/layouts/MobileLayout';
import Header from 'components/layouts/Header';
import { userContext, UserContextValues } from 'contexts/userContext';
import useContextValues from 'hooks/useContextValues';
import { useEffect } from 'react';
import useFetch from 'hooks/useFetch';
import { GetMeDataResponseBody } from 'types/userType';
import Spinner from 'components/@shared/Spinner';
import * as S from './App.styled';

const App = () => {
  const { login } = useContextValues<UserContextValues>(
    userContext
  ) as UserContextValues;

  const { data, loading } = useFetch<GetMeDataResponseBody>('/users/me');

  useEffect(() => {
    const accessToken = localStorage.getItem('accessToken');

    if (data && accessToken) {
      login(data, accessToken);
    }
  }, [data]);

  return (
    <>
      <div
        css={css`
          width: 100vw;
          height: 100vh;
          display: flex;
          justify-content: center;
          align-items: center;
        `}
      >
        <MobileLayout>
          {loading ? (
            <S.Layout>
              <S.SpinnerBox>
                <Spinner />
              </S.SpinnerBox>
            </S.Layout>
          ) : (
            <>
              <Header />
              <Router />
            </>
          )}
        </MobileLayout>
      </div>
      <div id="root-modal" />
    </>
  );
};

export default App;
