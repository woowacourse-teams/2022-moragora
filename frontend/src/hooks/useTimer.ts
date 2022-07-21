import { useState, useRef, useEffect, useCallback } from 'react';

const useTimer = (initialTimestamp: number) => {
  const requestAnimationRef = useRef<number>();
  const [startTimestamp, setStartTimestamp] = useState(initialTimestamp);
  const [elapsed, setElapsed] = useState(0);
  const [lastElapsed, setLastElapsed] = useState(0);
  const currentTimestamp = initialTimestamp + elapsed;

  const getElapsed = useCallback(() => {
    const currentDateMilliSeconds = Date.now();
    const elapsedMilliSeconds =
      lastElapsed + currentDateMilliSeconds - startTimestamp;

    setElapsed(elapsedMilliSeconds);
  }, [lastElapsed, startTimestamp]);

  useEffect(() => {
    requestAnimationRef.current = requestAnimationFrame(getElapsed);

    return () => {
      if (requestAnimationRef.current) {
        cancelAnimationFrame(requestAnimationRef.current);
      }
    };
  }, [requestAnimationRef.current, getElapsed]);

  return { currentTimestamp, elapsed };
};

export default useTimer;
