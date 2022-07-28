import { useState, useRef, useEffect, useCallback } from 'react';

const useTimer = (initialTimestamp: number, intervalMs: number = 1000) => {
  const requestAnimationRef = useRef<number>();
  const [startTimestamp, setStartTimestamp] = useState(initialTimestamp);
  const [elapsed, setElapsed] = useState(0);
  const [lastElapsed, setLastElapsed] = useState(0);
  const currentTimestamp = initialTimestamp + elapsed;

  const getElapsed = useCallback(() => {
    const currentDateMilliSeconds = Date.now();
    const elapsedMilliSeconds =
      lastElapsed + currentDateMilliSeconds - startTimestamp;

    setElapsed((prev) => {
      const shouldUpdate = elapsedMilliSeconds - prev >= intervalMs;

      return shouldUpdate ? elapsedMilliSeconds : prev;
    });
  }, [lastElapsed, startTimestamp]);

  useEffect(() => {
    setStartTimestamp(initialTimestamp);
    setElapsed(0);
  }, [initialTimestamp]);

  const startAnimationFrame = () => {
    getElapsed();
    requestAnimationRef.current = requestAnimationFrame(startAnimationFrame);
  };

  useEffect(() => {
    startAnimationFrame();

    return () => {
      if (requestAnimationRef.current) {
        cancelAnimationFrame(requestAnimationRef.current);
      }
    };
  }, []);

  return { currentTimestamp, elapsed };
};

export default useTimer;
