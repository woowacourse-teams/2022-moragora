import React from 'react';
import * as S from './Checkbox.styled';

type CheckboxProps = React.PropsWithChildren<
  Omit<React.InputHTMLAttributes<HTMLInputElement>, 'type'>
>;

const Checkbox: React.FC<CheckboxProps> = ({ children, ...props }) => {
  return (
    <S.Label>
      <S.Input type="checkbox" {...props} />
      {children}
    </S.Label>
  );
};

export default Checkbox;
