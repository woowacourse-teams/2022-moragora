import { useEffect, useRef } from 'react';
import useGeolocation from '../../hooks/useGeolocation';
import useKakaoMap from '../../hooks/useKakaoMap';
import Beacon from './Beacon';
import * as S from './GeolocationPage.styled';

function GelocationPage() {
  const { currentPosition, isLoading } = useGeolocation();
  const {
    mapContainerRef,
    beacons,
    removeBeacon,
    removeBeacons,
    setControllable,
    panTo,
  } = useKakaoMap();
  const mapOverlayRef = useRef<HTMLDivElement>(null);

  const handleCheckInClick = () => {};

  useEffect(() => {
    if (isLoading && mapOverlayRef.current) {
      mapOverlayRef.current.classList.add('loading');
    } else {
      mapOverlayRef.current?.classList.remove('loading');
      panTo(currentPosition.latitude, currentPosition.longitude);
    }

    setControllable(!isLoading);
  }, [
    isLoading,
    mapOverlayRef.current,
    setControllable,
    currentPosition.latitude,
    currentPosition.longitude,
    panTo,
  ]);

  return (
    <S.Layout>
      <S.MapSection>
        <S.Map id="map" ref={mapContainerRef}>
          <S.MapOverlay
            id="map-overlay"
            ref={mapOverlayRef}
            className="loading"
          >
            Loading...
          </S.MapOverlay>
        </S.Map>

        <S.BeaconListBox>
          <S.BeaconListLengthBox>
            <span>비콘 개수: {beacons.length}</span>
            <button type="button" onClick={removeBeacons}>
              reset
            </button>
          </S.BeaconListLengthBox>
          <S.BeaconList>
            {beacons.map(({ id, position, address, radius }) => (
              <li key={id}>
                <Beacon
                  id={id}
                  position={position}
                  address={address}
                  radius={Math.round(radius)}
                  panTo={panTo}
                  remove={removeBeacon}
                />
              </li>
            ))}
          </S.BeaconList>
        </S.BeaconListBox>
      </S.MapSection>
      <section>
        <button type="button" onClick={handleCheckInClick}>
          출석 체크
        </button>
        <p>현재 위도: {currentPosition.latitude}</p>
        <p>현재 경도: {currentPosition.longitude}</p>
      </section>
    </S.Layout>
  );
}

export default GelocationPage;
