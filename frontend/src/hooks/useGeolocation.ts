import { useEffect, useRef, useState } from 'react';

type GeolocationOptions = {
  defaultPosition: GeolocationPosition;
  options: PositionOptions;
  onWatchSuccess: PositionCallback;
};

const useGeolocation = ({
  defaultPosition,
  options,
  onWatchSuccess,
}: Partial<GeolocationOptions> = {}) => {
  const [permissionState, setPermissionState] = useState<PermissionState>();
  const [currentPosition, setCurrentPosition] = useState<
    GeolocationPosition | undefined
  >(defaultPosition);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<GeolocationPositionError>();
  const watchId = useRef<ReturnType<Geolocation['watchPosition']>>();

  const clearWatch = () => {
    if (!watchId.current) {
      return;
    }

    navigator.geolocation.clearWatch(watchId.current);
  };

  const requestPermission = () =>
    new Promise<void>((resolve, reject) => {
      navigator.permissions
        .query({ name: 'geolocation' })
        .then((result) => {
          setPermissionState(result.state);

          result.addEventListener('change', () => {
            setPermissionState(result.state);
          });

          resolve();
        })
        .catch((reason) => {
          reject(reason);
        });
    });

  const handleGetSuccess: PositionCallback = (position) => {
    setCurrentPosition(position);
    setIsLoading(false);
  };

  const handleGetError: PositionErrorCallback = (positionError) => {
    setError(positionError);
  };

  const handleWatchSuccess: PositionCallback = (position) => {
    setCurrentPosition(position);
    onWatchSuccess?.(position);
  };

  const handleWatchError: PositionErrorCallback = (positionError) => {
    setError(positionError);
  };

  useEffect(() => {
    setIsLoading(true);
    clearWatch();
    requestPermission()
      .then(() => {
        if (navigator.geolocation) {
          navigator.geolocation.getCurrentPosition(
            handleGetSuccess,
            handleGetError,
            options
          );

          if (onWatchSuccess) {
            navigator.geolocation.watchPosition(
              handleWatchSuccess,
              handleWatchError,
              options
            );
          }
        } else {
          throw new Error('GEOLOCATION_NOT_AVAILABLE');
        }
      })
      .catch((reason) => {
        if ((reason as typeof error)?.code) {
          if ((reason as typeof error)?.code === 1) {
            setPermissionState('denied');
          }

          setError(reason as typeof error);
        }

        setIsLoading(false);
      });
  }, [navigator.geolocation]);

  return {
    currentPosition,
    isLoading,
    error,
    permissionState,
    clearWatch,
    requestPermission,
  };
};

export default useGeolocation;
