import React from 'react';
import * as S from './RadioButton.styled';

type RadioButtonProps = Omit<
  React.InputHTMLAttributes<HTMLInputElement>,
  'type'
>;

const RadioButton: React.FC<RadioButtonProps> = (props) => {
  return <S.Input {...props} type="radio" />;
};

export default RadioButton;
