import styled from '@emotion/styled';

export const DivideLine = styled.hr`
  border: solid 1px ${({ theme: { colors } }) => colors['background']};
  border-width: 1px 0 0 0;
`;
