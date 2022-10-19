import { useCallback, useEffect, useRef, useState } from 'react';
import '../styles/KakaoMap/overlay.css';

const { kakao } = window;

// 마우스 우클릭 하여 원 그리기가 종료됐을 때 호출하여
// 그려진 원의 반경 정보와 반경에 대한 도보, 자전거 시간을 계산하여
// HTML Content를 만들어 리턴하는 함수입니다
const getTimeHTML = (distance: number) => `
  <div class="beacon-overlay">
      반경 <span class="radius-span">${distance}m</span>
  </div>
`;

export type Beacon = {
  id: number;
  position: { Ma: number; La: number };
  radius: number;
  marker: kakao.maps.Marker;
  polyline: kakao.maps.Polyline;
  circle: kakao.maps.Circle;
  overlay: kakao.maps.CustomOverlay;
  address: kakao.maps.services.Address;
};

const useKakaoMap = (
  defaultPosition = { latitude: 37.5152933, longitude: 127.1029866 }
) => {
  const mapContainerRef = useRef<HTMLDivElement>(null);
  const mapRef = useRef<kakao.maps.Map | null>(null);
  const geocoderRef = useRef(new kakao.maps.services.Geocoder());
  const [beacons, setBeacons] = useState<Beacon[]>([]);
  const [clickable, setClickable] = useState(false);
  const drawingControllerRef = useRef<{
    isDrawing: boolean;
    centerPosition: any | null;
    drawingMarker: kakao.maps.Marker | null;
    drawingDot: any | null;
    drawingCircle: kakao.maps.Circle | null;
    drawingLine: kakao.maps.Polyline | null;
    drawingOverlay: kakao.maps.CustomOverlay | null;
  }>({
    isDrawing: false,
    centerPosition: null,
    drawingMarker: null,
    drawingDot: null,
    drawingCircle: null,
    drawingLine: null,
    drawingOverlay: null,
  });

  const setCenter = useCallback((latitude: number, longitude: number) => {
    const map = mapRef.current;
    const moveLatLon = new kakao.maps.LatLng(latitude, longitude);

    map?.setCenter(moveLatLon);
  }, []);

  const panTo = useCallback((latitude: number, longitude: number) => {
    const map = mapRef.current;
    const moveLatLon = new kakao.maps.LatLng(latitude, longitude);

    map?.panTo(moveLatLon);
    map?.relayout();
  }, []);

  const setControllable = useCallback((controllable: boolean) => {
    if (mapRef.current) {
      mapRef.current.setDraggable(controllable);
      mapRef.current.setZoomable(controllable);
    }

    setClickable(controllable);
  }, []);

  const addArea = async (mouseEvent: kakao.maps.event.MouseEvent) => {
    const drawingController = drawingControllerRef.current;
    const map = mapRef.current;

    const clickPosition = mouseEvent.latLng;

    const marker = new kakao.maps.Marker({
      position: drawingController.centerPosition,
    });

    // 원의 반경을 표시할 선 객체를 생성합니다
    const polyline = new kakao.maps.Polyline({
      path: [drawingController.centerPosition, clickPosition], // 선을 구성하는 좌표 배열입니다. 원의 중심좌표와 클릭한 위치로 설정합니다
      strokeWeight: 3, // 선의 두께 입니다
      strokeColor: '#00a0e9', // 선의 색깔입니다
      strokeOpacity: 1, // 선의 불투명도입니다 0에서 1 사이값이며 0에 가까울수록 투명합니다
      strokeStyle: 'solid', // 선의 스타일입니다
    });

    // 원 객체를 생성합니다
    const circle = new kakao.maps.Circle({
      center: drawingController.centerPosition, // 원의 중심좌표입니다
      radius: polyline.getLength(), // 원의 반지름입니다 m 단위 이며 선 객체를 이용해서 얻어옵니다
      strokeWeight: 1, // 선의 두께입니다
      strokeColor: '#00a0e9', // 선의 색깔입니다
      strokeOpacity: 0.1, // 선의 불투명도입니다 0에서 1 사이값이며 0에 가까울수록 투명합니다
      strokeStyle: 'solid', // 선의 스타일입니다
      fillColor: '#00a0e9', // 채우기 색깔입니다
      fillOpacity: 0.2, // 채우기 불투명도입니다
    });

    const radius = Math.round(circle.getRadius()); // 원의 반경 정보를 얻어옵니다
    const content = getTimeHTML(radius); // 커스텀 오버레이에 표시할 반경 정보입니다

    // 반경정보를 표시할 커스텀 오버레이를 생성합니다
    const radiusOverlay = new kakao.maps.CustomOverlay({
      content, // 표시할 내용입니다
      position: clickPosition, // 표시할 위치입니다. 클릭한 위치로 설정합니다
      xAnchor: 0,
      yAnchor: 0,
      zIndex: 1,
    });

    // 마커, 원, 선, 반경 정보 커스텀 오버레이를 지도에 표시합니다.
    marker.setMap(map);
    circle.setMap(map);
    // polyline.setMap(map);
    radiusOverlay.setMap(map);

    const position = {
      ...drawingController.centerPosition,
    };

    try {
      const address = await coord2Address(position.La, position.Ma);

      // 배열에 추가합니다
      // 이 배열을 이용해서 "모두 지우기" 버튼을 클릭했을 때 지도에 그려진 마커, 원, 선, 커스텀오버레이들을 지웁니다
      setBeacons((prev) => {
        if (prev.length >= 3) {
          marker.setMap(null);
          circle.setMap(null);
          polyline.setMap(null);
          radiusOverlay.setMap(null);

          alert('비콘 등록은 최대 3개까지 가능합니다.');

          return prev;
        }

        return [
          ...prev,
          {
            id: Date.now(),
            position,
            radius: (
              drawingController.drawingCircle as kakao.maps.Circle
            ).getRadius(),
            marker,
            polyline,
            circle,
            overlay: radiusOverlay,
            address: address.address,
          },
        ];
      });
    } catch (e) {
      marker.setMap(null);
      circle.setMap(null);
      polyline.setMap(null);
      radiusOverlay.setMap(null);

      throw e;
    }
  };

  const coord2Address = (
    latitude: number,
    longitude: number,
    options?: {
      /**
       * 입력 좌표 체계. 기본값은 WGS84
       */
      input_coord: kakao.maps.services.Coords;
    }
  ): Promise<{
    /**
     * 지번 주소 상세 정보
     */
    address: kakao.maps.services.Address;
    /**
     * 도로명 주소 상세 정보
     */
    road_address: kakao.maps.services.RoadAaddress | null;
  }> =>
    new Promise((resolve, reject) => {
      geocoderRef.current.coord2Address(
        latitude,
        longitude,
        (result, status) => {
          if (status === kakao.maps.services.Status.OK) {
            resolve(result[0]);
          }

          reject(result);
        },
        options
      );
    });

  const handleClick = (mouseEvent: kakao.maps.event.MouseEvent) => {
    const drawingController = drawingControllerRef.current;
    const map = mapRef.current;

    if (!clickable) {
      return;
    }

    if (drawingController.isDrawing) {
      addArea(mouseEvent);

      // 그리기 상태를 그리고 있지 않는 상태로 바꿉니다
      drawingController.isDrawing = false;

      // 중심 좌표를 초기화 합니다
      drawingController.centerPosition = null;

      // 그려지고 있는 마커, 원, 선, 커스텀오버레이를 지도에서 제거합니다
      drawingController.drawingMarker?.setMap(null);
      drawingController.drawingCircle?.setMap(null);
      drawingController.drawingLine?.setMap(null);
      drawingController.drawingOverlay?.setMap(null);

      drawingController.drawingMarker = null;

      return;
    }

    // 클릭 이벤트가 발생했을 때 원을 그리고 있는 상태가 아니면 중심좌표를 클릭한 지점으로 설정합니다
    // 상태를 그리고있는 상태로 변경합니다
    drawingController.isDrawing = true;

    // 원이 그려질 중심좌표를 클릭한 위치로 설정합니다
    drawingController.centerPosition = mouseEvent.latLng;

    if (!drawingController.drawingMarker) {
      // 마커를 생성합니다
      drawingController.drawingMarker = new kakao.maps.Marker({
        position: drawingController.centerPosition,
      });

      drawingController.drawingMarker.setMap(map);
    }

    // 그려지고 있는 원의 반경을 표시할 선 객체를 생성합니다
    if (!drawingController.drawingLine) {
      drawingController.drawingLine = new kakao.maps.Polyline({
        path: [],
        strokeWeight: 3, // 선의 두께입니다
        strokeColor: '#00a0e9', // 선의 색깔입니다
        strokeOpacity: 0, // 선의 불투명도입니다 0에서 1 사이값이며 0에 가까울수록 투명합니다
        strokeStyle: 'solid', // 선의 스타일입니다
      });
    }

    if (!drawingController.drawingDot) {
      drawingController.drawingLine = new kakao.maps.Polyline({
        path: [],
        strokeWeight: 3, // 선의 두께입니다
        strokeColor: '#00a0e9', // 선의 색깔입니다
        strokeOpacity: 1, // 선의 불투명도입니다 0에서 1 사이값이며 0에 가까울수록 투명합니다
        strokeStyle: 'solid', // 선의 스타일입니다
      });
    }

    // 그려지고 있는 원을 표시할 원 객체를 생성합니다
    if (!drawingController.drawingCircle) {
      drawingController.drawingCircle = new kakao.maps.Circle({
        center: new kakao.maps.LatLng(0, 0),
        radius: 0,
        strokeWeight: 1, // 선의 두께입니다
        strokeColor: '#00a0e9', // 선의 색깔입니다
        strokeOpacity: 0.1, // 선의 불투명도입니다 0에서 1 사이값이며 0에 가까울수록 투명합니다
        strokeStyle: 'solid', // 선의 스타일입니다
        fillColor: '#00a0e9', // 채우기 색깔입니다
        fillOpacity: 0.2, // 채우기 불투명도입니다
      });
    }

    // 그려지고 있는 원의 반경 정보를 표시할 커스텀오버레이를 생성합니다
    if (!drawingController.drawingOverlay) {
      drawingController.drawingOverlay = new kakao.maps.CustomOverlay({
        position: new kakao.maps.LatLng(0, 0),
        xAnchor: 0,
        yAnchor: 0,
        zIndex: 1,
      });
    }
  };

  const handleMouseMove = (mouseEvent: kakao.maps.event.MouseEvent) => {
    const drawingController = drawingControllerRef.current;
    const map = mapRef.current;

    // 마우스무브 이벤트가 발생했을 때 원을 그리고있는 상태이면
    if (
      drawingController.isDrawing &&
      drawingController.drawingLine &&
      drawingController.drawingCircle &&
      drawingController.drawingOverlay
    ) {
      // 마우스 커서의 현재 위치를 얻어옵니다
      const mousePosition = mouseEvent.latLng;

      // 그려지고 있는 선을 표시할 좌표 배열입니다. 클릭한 중심좌표와 마우스커서의 위치로 설정합니다
      const linePath = [drawingController.centerPosition, mousePosition];

      // 그려지고 있는 선을 표시할 선 객체에 좌표 배열을 설정합니다
      drawingController.drawingLine.setPath(linePath);

      // 원의 반지름을 선 객체를 이용해서 얻어옵니다
      const length = drawingController.drawingLine.getLength();

      if (length > 0) {
        // 그려지고 있는 원의 중심좌표와 반지름입니다
        const circleOptions = {
          center: drawingController.centerPosition,
          radius: length,
        };

        // 그려지고 있는 원의 옵션을 설정합니다
        drawingController.drawingCircle.setOptions(circleOptions);

        // 반경 정보를 표시할 커스텀오버레이의 내용입니다
        const radius = Math.round(drawingController.drawingCircle.getRadius());
        const content = `<div class="beacon-overlay">반경 <span class="radius-span">${radius}</span>m</div>`;

        drawingController.drawingOverlay.setPosition(mousePosition);
        drawingController.drawingOverlay.setContent(content);

        drawingController.drawingCircle.setMap(map);
        // drawingController.drawingLine.setMap(map);
        drawingController.drawingOverlay.setMap(map);
      } else {
        drawingController.drawingCircle.setMap(null);
        drawingController.drawingLine.setMap(null);
        drawingController.drawingOverlay.setMap(null);
      }
    }
  };

  const removeBeacon = (id: Beacon['id']) => {
    const beacon = beacons.find((beacon) => beacon.id === id);

    if (!beacon) {
      console.error('no such beacon');

      return;
    }

    beacon.marker.setMap(null);
    beacon.circle.setMap(null);
    beacon.polyline.setMap(null);
    beacon.overlay.setMap(null);

    setBeacons((prev) => prev.filter((beacon) => beacon.id !== id));
  };

  // 지도에 표시되어 있는 모든 원과 반경정보를 표시하는 선, 커스텀 오버레이를 지도에서 제거합니다
  const removeBeacons = () => {
    beacons.forEach((beacon) => {
      beacon.marker.setMap(null);
      beacon.circle.setMap(null);
      beacon.polyline.setMap(null);
      beacon.overlay.setMap(null);
    });

    setBeacons([]);
  };

  useEffect(() => {
    if (!mapContainerRef.current) {
      return;
    }

    const mapContainer = mapContainerRef.current; // 지도를 표시할 div
    const mapOption = {
      center: new kakao.maps.LatLng(
        defaultPosition.latitude,
        defaultPosition.longitude
      ), // 지도의 중심좌표
      level: 2, // 지도의 확대 레벨
    };

    mapRef.current = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

    const map = mapRef.current;

    const { addListener } = kakao.maps.event;

    // 지도에 클릭 이벤트를 등록합니다
    addListener(map, 'click', handleClick);

    // 지도에 마우스무브 이벤트를 등록합니다
    // 원을 그리고있는 상태에서 마우스무브 이벤트가 발생하면 그려질 원의 위치와 반경정보를 동적으로 보여주도록 합니다
    addListener(map, 'mousemove', handleMouseMove);

    return () => {
      removeBeacons();
    };
  }, [
    defaultPosition.latitude,
    defaultPosition.longitude,
    mapContainerRef.current,
  ]);

  return {
    beacons,
    mapContainerRef,
    removeBeacon,
    removeBeacons,
    setControllable,
    setCenter,
    panTo,
  };
};

export default useKakaoMap;
