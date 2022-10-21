import styled from '@emotion/styled';

export const Layout = styled.div`
  width: fit-content;
  display: flex;
  flex-direction: column;
  padding: 0.75rem;
  border-radius: 1rem;
  background-color: ${({ theme: { colors } }) => colors['white']};
`;

export const CalendarBox = styled.div`
  display: grid;
  grid-template-columns: repeat(7, 2rem);
  grid-auto-rows: 2rem;
  grid-row-gap: 0.5rem;
  grid-column-gap: 0.5rem;

  ${({ theme: { media } }) => media['sm']`
    grid-template-columns: repeat(7, 3rem);
    grid-auto-rows: 3rem;
  `}

  ${({ theme: { media } }) => media['md']`
    grid-template-columns: repeat(7, 4rem);
    grid-auto-rows: 4rem;
  `}
`;
