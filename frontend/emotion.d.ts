import '@emotion/react';
import { SerializedStyles } from '@emotion/react';

type ScreenSize = 'xs' | 'sm' | 'md' | 'lg' | 'xl';
type MediaQuery<T extends string> = Record<
  T,
  (arg0: string[]) => SerializedStyles
>;

declare module '@emotion/react' {
  export interface Theme {
    colors: {
      normal: string;
      'subtle-dark': string;
      'subtle-light': string;
      background: string;
      surface: string;
      primary: string;
      'primary-subtle': string;
      black: string;
      white: string;
      tint: string;
      transparent: string;
    };
    media: MediaQuery<ScreenSize>;
  }
}
