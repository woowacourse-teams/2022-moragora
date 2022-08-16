import styled from '@emotion/styled';

export const Layout = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
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

export const LogoutButtonBox = styled.div`
  padding: 0 2rem;
`;
