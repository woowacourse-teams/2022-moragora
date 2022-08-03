import * as S from './CircleProgressBar.styled';
import React from 'react';

type CircleProgressBarProps = {
  size: number;
  percent: number;
};

const CircleProgressBar: React.FC<
  React.PropsWithChildren<CircleProgressBarProps>
> = ({ size, percent, children }) => {
  return (
    <S.Layout>
      <S.SVG size={size}>
        <S.BackgroundBar
          cx={`${size / 2}rem`}
          cy={`${size / 2}rem`}
          r={`${size / 2.3}rem`}
          size={size}
        />
        <S.MeterBar
          cx={`${size / 2}rem`}
          cy={`${size / 2}rem`}
          r={`${size / 2.3}rem`}
          radius={size / 2.3}
          size={size}
          percent={percent > 100 ? 100 : percent}
        />
      </S.SVG>
      <S.ContentBox size={size}>{children}</S.ContentBox>
    </S.Layout>
  );
};

export default CircleProgressBar;
