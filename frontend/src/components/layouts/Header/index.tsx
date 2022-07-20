import { useContext } from 'react';
import { Route, Routes, useNavigate } from 'react-router-dom';
import * as S from './Header.styled';
import ChevronLeftIconSVG from 'assets/chevron-left.svg';
import Avatar from 'components/@shared/Avatar';
import { userContext } from 'contexts/userContext';

const Header = () => {
  const navigate = useNavigate();
  const userState = useContext(userContext);

  const handleClick = () => {
    navigate(-1);
  };

  return (
    <S.Layout>
      <Routes>
        <Route
          path="/meeting"
          element={
            <S.AvatarMessageBox>
              <S.Box>
                <Avatar />
              </S.Box>
              <S.WelcomeMessageBox>
                <p>반갑습니다.</p>
                <S.NicknameParagraph>
                  {userState?.user?.nickname || 'unknown'}
                </S.NicknameParagraph>
              </S.WelcomeMessageBox>
            </S.AvatarMessageBox>
          }
        />
        <Route
          path="*"
          element={
            <S.Box>
              <S.BackwardButton type="button" onClick={handleClick}>
                <S.ChevronLeftImage src={ChevronLeftIconSVG} />
              </S.BackwardButton>
            </S.Box>
          }
        />
      </Routes>
      <S.Box />
    </S.Layout>
  );
};

export default Header;
