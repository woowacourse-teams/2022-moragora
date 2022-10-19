import { useEffect, useState } from 'react';

const useGeolocation = (defaultPosition?: GeolocationPosition) => {
  const [currentPosition, setCurrentPosition] = useState<
    GeolocationPosition | undefined
  >(defaultPosition);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<Error>();

  const handleSuccess: PositionCallback = (position) => {
    setCurrentPosition(position);

    setIsLoading(false);
  };

  const handleError: PositionErrorCallback = (err) => {
    throw err;
  };

  useEffect(() => {
    setIsLoading(true);

    try {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(handleSuccess, handleError);
      } else {
        throw new Error('위치 정보 사용 불가');
      }
    } catch (err) {
      if (err instanceof Error) {
        setError(err);
      }

      setIsLoading(false);
    }
  }, [navigator.geolocation]);

  return { currentPosition, isLoading, error };
};

export default useGeolocation;
