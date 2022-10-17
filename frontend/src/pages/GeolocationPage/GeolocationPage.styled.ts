import styled from '@emotion/styled';

export const Layout = styled.div``;

export const MapSection = styled.section`
  width: 100%;
  height: auto;
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 2rem;
  overflow: hidden;
`;

export const Map = styled.div`
  width: 100%;
  max-width: 700px;
  height: 500px;
`;

export const MapOverlay = styled.div`
  position: absolute;
  inset: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 2rem;
  z-index: 50;
  color: white;
  backdrop-filter: blur(0.5rem) brightness(90%);
  visibility: hidden;
  opacity: 0;

  transition: all 0.5s;

  & .loading {
    visibility: visible;
    opacity: 1;
  }
`;

export const BeaconListBox = styled.div`
  display: flex;
  flex-direction: column;
  width: 200px;
`;

export const BeaconListLengthBox = styled.div`
  display: flex;
  justify-content: space-between;
  margin: 1rem 0;
`;

export const BeaconList = styled.ul`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  overflow-x: hidden;
  overflow-y: scroll;
`;
