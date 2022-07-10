import { css, SerializedStyles, Theme } from '@emotion/react';

// type MediaQuery<T> = {
//   [k in keyof T]: (arg0: string[]) => SerializedStyles;
// };

const COLORS = {
  normal: '#19191B',
  'subtle-dark': '#61646B',
  'subtle-light': '#AFB1B6',
  background: '#EFEFF0',
  surface: '#FAFAFA',
  primary: '#3A00E5',
  'primary-subtle': '#9c7ff2',
  black: '#000000',
  white: '#ffffff',
  tint: 'rgba(0, 0, 0, 0.4)',
  transparent: 'transparent',
};

const SCREEN_SIZES = {
  xs: 480,
  sm: 768,
  md: 1024,
  lg: 1200,
  xl: 1680,
};

const media = {
  xs: (...args) => css`
    @media only screen and (max-width: ${SCREEN_SIZES.xs}px) {
      ${args}
    }
  `,
  sm: (...args) => css`
    @media only screen and (min-width: ${SCREEN_SIZES.xs}px) and (max-width: ${SCREEN_SIZES.sm}px) {
      ${args}
    }
  `,
  md: (...args) =>
    css`
      @media only screen and (min-width: ${SCREEN_SIZES.md}px) and (max-width: ${SCREEN_SIZES.lg}px) {
        ${args}
      }
    `,
  lg: (...args) =>
    css`
      @media only screen and (min-width: ${SCREEN_SIZES.lg}px) and (max-width: ${SCREEN_SIZES.xl}px) {
        ${args}
      }
    `,
  xl: (...args) =>
    css`
      @media only screen and (min-width: ${SCREEN_SIZES.xl}px) {
        ${args}
      }
    `,
};

export const theme: Theme = {
  colors: COLORS,
  media,
};
