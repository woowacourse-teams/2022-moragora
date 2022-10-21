import { css, Theme } from '@emotion/react';

const colors: Theme['colors'] = {
  normal: '#19191B',
  'subtle-dark': '#61646B',
  'subtle-light': '#AFB1B6',
  background: '#EFEFF0',
  surface: '#FAFAFA',
  primary: '#F19528',
  'primary-subtle': '#EAD08E',
  black: '#000000',
  white: '#ffffff',
  tint: 'rgba(0, 0, 0, 0.4)',
  transparent: 'transparent',
};

export const SCREEN_SIZES = {
  xs: 480,
  sm: 768,
  md: 1024,
  lg: 1200,
  xl: 1680,
};

const zIndex = {
  modal: 100,
  'modal-background': 90,
  footer: 50,
  header: 50,
  'slightly-float': 10,
  surface: 0,
};

const media: Theme['media'] = {
  xs: (...args) => css`
    @media only screen and (max-width: ${SCREEN_SIZES.xs}px) {
      ${args}
    }
  `,
  sm: (...args) => css`
    @media only screen and (min-width: ${SCREEN_SIZES.xs}px) {
      ${args}
    }
  `,
  md: (...args) =>
    css`
      @media only screen and (min-width: ${SCREEN_SIZES.md}px) {
        ${args}
      }
    `,
  lg: (...args) =>
    css`
      @media only screen and (min-width: ${SCREEN_SIZES.lg}px) {
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
  colors,
  media,
  zIndex,
};
