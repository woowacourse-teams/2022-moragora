import styled from '@emotion/styled';

export const AvatarFigure = styled.figure`
  width: 100%;
  margin: 0;
  padding: 1rem 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-bottom: 1px solid lightgray;
`;

export const AvatarImage = styled.img`
  width: 4rem;
  border: 2px solid lightgray;
  border-radius: 50%;
`;

export const AvatarCaption = styled.figcaption`
  margin-top: 0.75rem;
  font-size: 0.75rem;
  color: gray;
`;

export const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

export const Label = styled.label`
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  color: gray;
`;
