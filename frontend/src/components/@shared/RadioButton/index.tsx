import React, { forwardRef } from 'react';
import * as S from './RadioButton.styled';

type RadioButtonProps = Omit<
  React.InputHTMLAttributes<HTMLInputElement>,
  'type'
>;

const RadioButton = forwardRef<HTMLInputElement, RadioButtonProps>(
  (props, ref) => {
    return <S.Input {...props} ref={ref} type="radio" />;
  }
);

export default RadioButton;
