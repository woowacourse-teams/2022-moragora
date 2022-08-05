import styled from '@emotion/styled';

const Layout = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
  width: 414px;
  height: 896px;
  border-radius: 40px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.25);
  background-color: ${({ theme: { colors } }) => colors['surface']};
  overflow: hidden;
`;

export default Layout;
