import styled from '@emotion/styled';
import _Button from 'components/@shared/Button';
import Input from 'components/@shared/Input';

export const Layout = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
  width: 18rem;
  padding: 1.5rem;
  gap: 1.5rem;
  border: 2px solid ${({ theme: { colors } }) => colors['primary']};
  border-radius: 0.5rem;
  background-color: ${({ theme: { colors } }) => colors['white']};
  z-index: 50;
`;

export const Paragraph = styled.p`
  display: flex;
  flex-direction: column;
  font-weight: 700;
  font-size: 1.25rem;
`;

export const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
`;

export const InputBox = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid lightgray;
`;

export const NumberInput = styled(Input)`
  border: none;
  font-size: 2rem;
  padding: 0;
  width: 100%;

  :focus {
    outline: none;
  }

  :disabled {
    background-color: transparent;
  }

  ::-webkit-outer-spin-button,
  ::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
  }
`;

export const ExpiredTimeParagraph = styled.p`
  color: gray;
`;

export const ReSendButton = styled(_Button)`
  padding: 0;
  height: 2rem;
  font-size: 0.75rem;

  background-color: ${({ theme: { colors } }) => colors['primary-subtle']};
  &:hover {
    background-color: ${({ theme: { colors } }) => colors['primary']};
  }
`;

export const ConfirmButton = styled(_Button)`
  padding: 0;
  height: 2rem;
  font-size: 0.75rem;
`;

export const CloseButton = styled.button`
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
  font-size: 1.5rem;
  color: gray;
`;
