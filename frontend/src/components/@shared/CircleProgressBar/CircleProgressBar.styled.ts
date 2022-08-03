import { keyframes } from '@emotion/react';
import styled from '@emotion/styled';

export const Layout = styled.div`
  position: relative;
  display: inline-block;
`;

export const SVG = styled.svg<{ size: number }>`
  width: ${(props) => `${props.size}rem`};
  height: ${(props) => `${props.size}rem`};
  margin: 0.3rem;
`;

export const ContentBox = styled.div<{ size: number }>`
  position: absolute;
  display: flex;
  justify-content: center;
  width: ${({ size }) => `${size}rem`};
  top: 50%;
  left: 52%;
  transform: translate(-50%, -50%);
`;

export const BackgroundBar = styled.circle<{ size: number }>`
  fill: none;
  stroke-width: ${({ size }) => `${size / 20}rem`};
  stroke: ${({ theme: { colors } }) => colors['primary-subtle']};
`;

const computeProgress = (percent: number, radius: number) => {
  return `${2 * Math.PI * radius * (1 - percent / 100)}rem`;
};

const progress = (percent: number, radius: number) => keyframes`
  from {
        stroke-dashoffset: ${computeProgress(0, radius)};
    }
    to {
        stroke-dashoffset: ${computeProgress(percent, radius)};

    }
`;

const shadow = (percent: number) => {
  return (
    percent === 100 &&
    keyframes`
      from {
        filter: none;
      }to{
        filter: drop-shadow(0 0 0.4rem orange);
      }
    `
  );
};

export const MeterBar = styled.circle<{
  percent: number;
  size: number;
  radius: number;
}>`
  fill: none;
  stroke-width: ${({ size }) => `${size / 20}rem`};
  stroke-linecap: round;
  transform: rotate(-90deg);
  transform-origin: 50% 50%;

  stroke-dasharray: ${({ radius }) => computeProgress(0, radius)};
  stroke-dashoffset: ${({ percent, radius }) =>
    computeProgress(percent, radius)};
  stroke: ${({ theme: { colors } }) => colors['primary']};
  animation: ${({ percent, radius }) => progress(percent, radius)} 1s ease-out,
    ${({ percent }) => shadow(percent)} 0.7s ease-out;
  animation-delay: 0s, 1s;
  animation-fill-mode: forwards;
`;
