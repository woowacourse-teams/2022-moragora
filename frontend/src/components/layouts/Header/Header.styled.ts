import styled from '@emotion/styled';

export const Layout = styled.div`
  height: 2.5rem;
  padding: 1rem 1rem;
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
  font-weight: 700;
`;

export const BackwardButton = styled.button`
  color: inherit;
`;

export const ChevronLeftImage = styled.img`
  height: 1.5rem;
  width: 1.5rem;
`;
