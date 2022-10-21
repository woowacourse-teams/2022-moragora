import styled from '@emotion/styled';

export const Layout = styled.div`
  padding: 0.75rem;
  margin: auto;
  display: flex;
  gap: 1rem;
  flex-direction: column;

  ${({ theme: { media } }) => media['md']`
    flex-direction: row;
  `}
`;

export const SpinnerBox = styled.div`
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
`;

export const CalendarBox = styled.div`
  display: flex;
  justify-content: center;
`;

export const CalendarSideSection = styled.section`
  ${({ theme: { media } }) => media['md']`
    width: 350px;
    flex-direction: row;
  `}
`;

export const CalendarControlHintParagraph = styled.p`
  color: ${({ theme: { colors } }) => colors['subtle-dark']};
`;

export const CalendarControlTitle = styled.h2`
  margin-bottom: 1rem;
`;

export const CalenderControlBox = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

export const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
`;

export const FieldGroupBox = styled.div`
  display: flex;
  gap: 0.5rem;
`;

export const FieldBox = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

export const Label = styled.label`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  color: ${({ theme: { colors } }) => colors['subtle-dark']};
`;

export const ButtonBox = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
`;
