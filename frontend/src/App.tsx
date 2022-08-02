import { useContext } from 'react';
import Router from 'router';
import { css } from '@emotion/react';
import * as S from './App.styled';
import MobileLayout from 'components/layouts/MobileLayout';
import Header from 'components/layouts/Header';
import Spinner from 'components/@shared/Spinner';
import { userContext, UserContextValues } from 'contexts/userContext';
import useQuery from 'hooks/useQuery';
import { getLoginUserDataApi } from 'apis/userApis';

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
          {isLoading ? (
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
