import { css } from '@emotion/react';

const reset = css`
  /**
   * Remove button styles.
   */

  button,
  input[type='submit'],
  input[type='reset'] {
    background: none;
    border: none;
    padding: 0;
    font: inherit;
    cursor: pointer;
  }
  input[type='search'] {
    outline-offset: 0;
  }

  h1,
  h2,
  h3,
  h4 {
    margin: 0;
  }

  p {
    margin: 0;
  }

  hr {
    padding: 0;
    margin: 0;
  }
`;

export default reset;
