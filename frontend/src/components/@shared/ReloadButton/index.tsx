import React from 'react';
import * as S from './ReloadButton.styled';

type ReloadButtonProps = Pick<
  React.ButtonHTMLAttributes<HTMLButtonElement>,
  'onClick' | 'disabled'
>;

const ReloadButton: React.FC<ReloadButtonProps> = (props) => {
  return (
    <S.Button type="button" {...props}>
      <S.IconSVG
        xmlns="http://www.w3.org/2000/svg"
        fill="none"
        viewBox="0 0 24 24"
        stroke="currentColor"
        strokeWidth={2}
      >
        <path
          strokeLinecap="round"
          strokeLinejoin="round"
          d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"
        />
      </S.IconSVG>
    </S.Button>
  );
};

export default ReloadButton;
