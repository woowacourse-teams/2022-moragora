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
          background-color: #fafafa;
        }

        .noselect {
          -webkit-touch-callout: none; /* iOS Safari */
          -webkit-user-select: none; /* Safari */
          -khtml-user-select: none; /* Konqueror HTML */
          -moz-user-select: none; /* Old versions of Firefox */
          -ms-user-select: none; /* Internet Explorer/Edge */
          user-select: none; /* Non-prefixed version, currently
                                  supported by Chrome, Edge, Opera and Firefox */
        }
      `}
    />
  </>
);

export default GlobalStyles;
