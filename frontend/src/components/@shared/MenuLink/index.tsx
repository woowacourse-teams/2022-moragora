import React from 'react';
import { LinkProps } from 'react-router-dom';
import * as S from './MenuLink.styled';

type MenuLinkProps = {
  icon?: React.ReactNode;
  disabled?: boolean;
} & LinkProps;

const MenuLink: React.FC<MenuLinkProps> = ({
  icon,
  children,
  disabled,
  ...props
}) => {
  return (
    <S.LayoutLink disabled={disabled} {...props}>
      <S.ContentBox>
        {icon}
        {children}
      </S.ContentBox>
      {!disabled && (
        <S.RightChevronIconSVG
          xmlns="http://www.w3.org/2000/svg"
          width="1rem"
          height="1rem"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          strokeWidth={2}
        >
          <path strokeLinecap="round" strokeLinejoin="round" d="M9 5l7 7-7 7" />
        </S.RightChevronIconSVG>
      )}
    </S.LayoutLink>
  );
};

export default MenuLink;
