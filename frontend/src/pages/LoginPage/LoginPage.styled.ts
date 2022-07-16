import styled from '@emotion/styled';
import Button from '../../components/@shared/Button';
import { Link } from 'react-router-dom';

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

export const InputHint = styled.p`
  font-size: 0.5rem;
`;

export const ButtonBox = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  align-items: center;
`;

export const RegisterHintParagraph = styled.p``;

export const RegisterLink = styled(Link)`
  margin-left: 0.5rem;

  text-decoration: inherit;
  color: ${({ theme: { colors } }) => colors['primary']};
`;

export const LoginButton = styled(Button)`
  width: 382px;
`;
