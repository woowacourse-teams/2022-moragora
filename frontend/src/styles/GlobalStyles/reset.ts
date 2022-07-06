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
`;

export default reset;
