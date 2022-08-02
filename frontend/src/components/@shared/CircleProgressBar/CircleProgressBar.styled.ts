import { css, keyframes } from '@emotion/react';
import styled from '@emotion/styled';

export const Layout = styled.div`
  position: relative;
  display: inline-block;
`;

export const SVG = styled.svg<{ size: number }>`
  width: ${(props) => `${props.size}px`};
  height: ${(props) => `${props.size}px`};
  margin: 0.3rem;
`;

export const ContentBox = styled.div<{ size: number }>`
  position: absolute;
  display: flex;
  justify-content: center;
  width: ${({ size }) => `${size}px`};
  top: 50%;
  left: 52%;
  transform: translate(-50%, -50%);
`;

export const BackgroundBar = styled.circle<{ size: number }>`
  fill: none;
  stroke-width: ${({ size }) => `${size / 20}px`};
  stroke: ${({ theme: { colors } }) => colors['primary-subtle']};
`;

const computeProgress = (percent: number, r: number) => {
  return 2 * Math.PI * r * (1 - percent / 100);
};

const progress = (percent: number, r: number) => keyframes`
  from {
        stroke-dashoffset: ${computeProgress(0, r)};
    }
    to {
        stroke-dashoffset: ${computeProgress(percent, r)};

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
  r: number;
}>`
  fill: none;
  stroke-width: ${({ size }) => `${size / 20}px`};
  stroke-linecap: round;
  transform: rotate(-90deg);
  transform-origin: 50% 50%;

  stroke-dasharray: ${({ r }) => computeProgress(0, r)};
  stroke-dashoffset: ${({ percent, r }) => computeProgress(percent, r)};
  stroke: ${({ theme: { colors } }) => colors['primary']};
  animation: ${({ percent, r }) => progress(percent, r)} 1s ease-out,
    ${({ percent }) => shadow(percent)} 0.7s ease-out;
  animation-delay: 0s, 1s;
  animation-fill-mode: forwards;
`;
