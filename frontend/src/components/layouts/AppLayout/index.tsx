import React, { useEffect, useState } from 'react';
import { debounce } from 'utils/debounce';
import * as S from './AppLayout.styled';

const AppLayout: React.FC<React.PropsWithChildren> = ({ children }) => {
  const [appHeight, setAppHeight] = useState(window.innerHeight);

  useEffect(() => {
    window.addEventListener(
      'resize',
      debounce(() => {
        setAppHeight(window.innerHeight);
      }, 100)
    );
  }, []);

  return <S.AppLayout appHeight={appHeight}>{children}</S.AppLayout>;
};

export default AppLayout;
