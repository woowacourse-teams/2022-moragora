import React from 'react';
import * as S from './Toggle.styled';

type ToggleProps = React.PropsWithChildren<
  Omit<React.InputHTMLAttributes<HTMLInputElement>, 'type'>
>;

const Toggle: React.FC<ToggleProps> = ({ children, ...props }) => {
  return (
    <S.Label>
      {children}
      <S.Input type="checkbox" {...props} />
    </S.Label>
  );
};

export default Toggle;
