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
  const [currentPosition, setCurrentPosition] = useState<
    GeolocationPosition | undefined
  >(defaultPosition);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<Error | GeolocationPositionError>();
  const watchId = useRef<ReturnType<Geolocation['watchPosition']>>();

  const clearWatch = () => {
    if (!watchId.current) {
      return;
    }

    navigator.geolocation.clearWatch(watchId.current);
  };

  const handleGetSuccess: PositionCallback = (position) => {
    setCurrentPosition(position);

    setIsLoading(false);
  };

  const handleGetError: PositionErrorCallback = (positionError) => {
    throw positionError;
  };

  const handleWatchSuccess: PositionCallback = (position) => {
    setCurrentPosition(position);
    onWatchSuccess?.(position);
  };

  const handleWatchError: PositionErrorCallback = (positionError) => {
    throw positionError;
  };

  useEffect(() => {
    setIsLoading(true);
    clearWatch();

    try {
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
    } catch (err) {
      if ((err as typeof error)?.message) {
        setError(err as typeof error);
      }

      setIsLoading(false);
    }
  }, [navigator.geolocation]);

  return { currentPosition, isLoading, error, clearWatch };
};

export default useGeolocation;
