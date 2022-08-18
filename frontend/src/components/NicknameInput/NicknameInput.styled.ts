import styled from '@emotion/styled';

export const Label = styled.label`
  position: relative;
  width: fit-content;
`;

export const NicknameInput = styled.input`
  padding: 0.2rem 0.3rem;
  width: 13rem;

  font-weight: 700;
  text-align: center;

  border: none;
  outline: none;
  background-color: transparent;

  ::placeholder {
    text-align: center;
  }
`;

export const EditIconImg = styled.img`
  position: absolute;
  top: -1.5rem;
  right: -1.5rem;
  padding: 0.2rem 0.3rem;
`;

export const InputUnderlineDiv = styled.div`
  position: absolute;
  left: 0;
  right: 0;
  bottom: -0.2rem;
  margin: auto;

  width: fit-content;
  height: 1px;

  font-weight: 700;
  text-align: center;
  color: transparent;

  border: none;

  ${NicknameInput}:focus ~ & {
    background-color: ${({ theme: { colors } }) => colors['subtle-light']};

    ${EditIconImg} {
      opacity: 0;
    }
  }
`;
