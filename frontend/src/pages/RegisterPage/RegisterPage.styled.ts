import styled from '@emotion/styled';
import { Link } from 'react-router-dom';
import DialogButton from 'components/@shared/DialogButton';
import Input from 'components/@shared/Input';

export const Layout = styled.div`
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  gap: 1.5rem;
`;

export const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 0.75rem;
`;

export const FieldBox = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
`;

export const Label = styled.label`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  overflow: hidden;
`;

export const EmailBox = styled.div`
  display: flex;
  gap: 0.75rem;
`;

export const EmailInput = styled(Input)`
  min-width: 0;
  flex: 1;
`;

export const EmailCheckButton = styled(DialogButton)`
  border-radius: 0.5rem;
`;

export const ButtonBox = styled.div`
  padding: 0 0.75rem;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  align-items: center;
`;

export const LoginHintParagraph = styled.p``;

export const LoginLink = styled(Link)`
  margin-left: 0.5rem;

  text-decoration: inherit;
  color: ${({ theme: { colors } }) => colors['primary']};
`;
