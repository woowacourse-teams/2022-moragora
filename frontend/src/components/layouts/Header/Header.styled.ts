import styled from '@emotion/styled';

export const Layout = styled.header`
  height: 3rem;
  padding: 0.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
`;

export const Box = styled.div`
  display: flex;
  align-items: center;
`;

export const AvatarMessageBox = styled.div`
  display: flex;
  gap: 1rem;
`;

export const WelcomeMessageBox = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 0.25rem;
`;

export const NicknameParagraph = styled.p`
  font-size: 1rem;
  font-weight: 700;
`;

export const BackwardButton = styled.button`
  color: inherit;
`;

export const ChevronLeftImage = styled.img`
  height: 1.5rem;
  width: 1.5rem;
`;
