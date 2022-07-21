import { css, Global } from '@emotion/react';
import normalize from './normalize';
import reset from './reset';
import fontFamily from './fontFamily';
import keyframes from './keyframes';

const GlobalStyles = () => (
  <>
    <Global styles={normalize} />
    <Global styles={reset} />
    <Global styles={fontFamily} />
    <Global styles={keyframes} />
    <Global
      styles={css`
        body {
          color: #19191b;
        }
      `}
    />
  </>
);

export default GlobalStyles;
