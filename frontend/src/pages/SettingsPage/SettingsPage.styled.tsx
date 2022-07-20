import styled from '@emotion/styled';

export const Layout = styled.div`
  display: flex;
  flex-direction: column;
  flex: 1;
  overflow: hidden;
  gap: 5rem;
`;

export const AvatarBox = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
`;

export const Image = styled.img`
  width: 10rem;
  height: 10rem;
`;

export const AvatarBorder = styled.div`
  border: 4px solid lightgray;
  border-radius: 50%;
`;

export const LogoutButtonBox = styled.div`
  padding: 0 2rem;
`;
