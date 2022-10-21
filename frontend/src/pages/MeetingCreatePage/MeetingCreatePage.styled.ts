import styled from '@emotion/styled';
import Button from 'components/@shared/Button';
import { SCREEN_SIZES } from 'styles/themes/theme';

export const Layout = styled.div`
  display: flex;
  flex-direction: column;
  overflow: hidden;
  padding: 0.75rem;
`;

export const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

export const FieldGroupBox = styled.div`
  display: flex;
  gap: 1rem;
`;

export const FieldBox = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

export const Label = styled.label`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
`;

export const AddMemberParagraph = styled.p`
  display: flex;
  justify-content: space-between;
`;

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
  height: 300px;

  @media only screen and (min-width: ${SCREEN_SIZES.md}px) {
    height: 400px;
  }
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

  &.loading {
    visibility: visible;
    opacity: 1;
  }
`;

export const BeaconListBox = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

export const BeaconListLengthBox = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 1rem;
`;

export const BeaconCountParagraph = styled.p`
  border-radius: 0.75rem;
  padding: 0.75rem 1rem;
  background-color: ${({ theme: { colors } }) => colors['white']};
  flex: 1;
`;

export const BeaconCountMaximumSpan = styled.span`
  color: ${({ theme: { colors } }) => colors['subtle-light']};
`;

export const BeaconList = styled.ul`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  list-style: none;
  padding: 0;
  margin: 0;
  background-color: inherit;
`;

export const MeetingCreateButton = styled(Button)`
  margin-top: 1rem;
`;

export const BeaconDescription = styled.p`
  font-size: 0.75rem;
  color: ${({ theme: { colors } }) => colors['subtle-dark']};
`;
