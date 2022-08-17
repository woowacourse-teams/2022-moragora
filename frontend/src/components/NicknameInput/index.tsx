import React, { forwardRef } from 'react';
import * as S from './NicknameInput.styled';
import EditIconSVG from 'assets/Edit.svg';

type NicknameInputProps = React.InputHTMLAttributes<HTMLInputElement> & {
  nickname: string;
};

const NicknameInput = forwardRef<HTMLInputElement, NicknameInputProps>(
  ({ nickname, ...props }, ref) => {
    return (
      <S.Label>
        <S.NicknameInput ref={ref} {...props} />
        <S.InputUnderlineDiv className="noselect">
          {nickname}
          <S.EditIconImg src={EditIconSVG} />
        </S.InputUnderlineDiv>
      </S.Label>
    );
  }
);

export default NicknameInput;
