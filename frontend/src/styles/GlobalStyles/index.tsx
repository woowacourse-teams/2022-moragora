import React from 'react';
import { Global } from '@emotion/react';
import normalize from './normalize';
import reset from './reset';
import fontFamily from './fontFamily';

const GlobalStyles = () => (
  <>
    <Global styles={normalize} />
    <Global styles={reset} />
    <Global styles={fontFamily} />
  </>
);

export default GlobalStyles;
