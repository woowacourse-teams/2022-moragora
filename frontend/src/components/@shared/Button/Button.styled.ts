import styled from '@emotion/styled';

const StyledButton = styled.button`
  width: 100%;
  background-color: black;
  color: white;
  padding: 0.75rem 0;
  border-radius: 1.5rem;

  :hover {
    background-color: gray;
  }

  :disabled {
    background-color: lightgray;
    color: gray;
    pointer-events: none;
  }
`;

export default StyledButton;
