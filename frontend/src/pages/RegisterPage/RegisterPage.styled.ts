import styled from '@emotion/styled';
import Button from '../../components/@shared/Button';
import DialogButton from '../../components/@shared/DialogButton';
import Input from '../../components/@shared/Input';

export const Layout = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
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

export const Label = styled.div`
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
  flex: 1;
`;

export const EmailCheckButton = styled(DialogButton)`
  border-radius: 0.5rem;
`;

export const ButtonBox = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  align-items: center;
`;

export const RegisterButton = styled(Button)`
  width: 382px;
`;

export const LoginHintParagraph = styled.p``;

export const LoginLink = styled.a`
  margin-left: 0.5rem;

  text-decoration: none;
  color: ${({ theme: { colors } }) => colors['primary']};
`;
