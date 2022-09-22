import styled from '@emotion/styled';

export const Layout = styled.div`
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  gap: 1rem;
`;

export const ProfileBox = styled.div`
  padding: 2rem 0;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  align-items: center;
`;

export const AvatarBox = styled.div`
  border: 4px solid ${({ theme: { colors } }) => colors['subtle-light']};
  border-radius: 50%;
`;

export const MenuLinkBox = styled.div`
  padding: 0 0.75rem;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
`;

export const EmailSpan = styled.span`
  color: ${({ theme: { colors } }) => colors['subtle-light']};
`;
