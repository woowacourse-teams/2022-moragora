import styled from '@emotion/styled';

export const Layout = styled.div`
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
`;

export const SectionTitle = styled.h2`
  font-size: 1.25rem;
  font-weight: 400;

  padding: 0 0.75rem;
`;

export const SpinnerBox = styled.div`
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
`;

export const ErrorBox = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 2rem;
`;

export const CheckTimeSection = styled.section``;

export const MeetingList = styled.ul`
  list-style: none;
  display: flex;
  align-items: center;
  padding: 0;
  gap: 1rem;
  overflow-x: scroll;
  overflow-y: hidden;
  padding: 0 0.75rem;

  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */

  &::-webkit-scrollbar {
    display: none; /* Chrome, Safari, Opera*/
  }
`;

export const MeetingItem = styled.div`
  display: inline-block;
`;

export const EmptyStateBox = styled.div`
  flex: 1;
  padding: 1.25rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 1.5rem;
`;

export const EmptyStateImage = styled.img`
  margin-top: -4rem;
  padding-bottom: 2rem;
  width: 10rem;
  height: 10rem;
  opacity: 0;
  animation: smooth-appear 2s;
  animation-fill-mode: forwards;
`;

export const EmptyStateTitle = styled.h2`
  font-size: 1.25rem;
  font-weight: 600;
  color: ${({ theme: { colors } }) => colors['subtle-dark']};
  opacity: 0;
  animation: smooth-appear 2s;
  animation-fill-mode: forwards;
`;

export const EmptyStateParagraph = styled.p`
  font-size: 1rem;
  margin-bottom: 3rem;
  color: ${({ theme: { colors } }) => colors['subtle-light']};
  opacity: 0;
  animation: dissolve 1s;
  animation-delay: 1.5s;
  animation-fill-mode: forwards;
`;

export const MapSection = styled.section`
  width: 100%;
  height: auto;
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 2rem;
  overflow: hidden;
  margin-bottom: 1rem;
`;

export const Map = styled.div`
  width: 100%;
  height: 400px;
`;

export const MapOverlay = styled.div`
  position: absolute;
  inset: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 2rem;
  z-index: ${({ theme: { zIndex } }) => zIndex['slightly-float']};
  color: white;
  backdrop-filter: blur(0.5rem) brightness(90%);
  visibility: hidden;
  opacity: 0;

  transition: all 0.5s;

  &.loading {
    visibility: visible;
    opacity: 1;
  }
`;

export const MapUserMarker = styled.svg`
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -100%);
  z-index: ${({ theme: { zIndex } }) => zIndex['slightly-float']};
  color: ${({ theme: { colors } }) => colors['primary']};
  visibility: visible;
  opacity: 1;
  transition: all 0.5s ease-out;

  &.loading {
    transform: translate(-50%, -200%);
    visibility: hidden;
    opacity: 0;
  }
`;
