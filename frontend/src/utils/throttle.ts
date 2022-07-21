export const throttle = <T extends (...args: any[]) => any>(
  callback: T,
  wait: number = 0
) => {
  let timerId: NodeJS.Timeout | null = null;
  let lastArgs: {
    current: Parameters<T> | [];
  } = {
    current: [],
  };

  return (...args: Parameters<T>) => {
    lastArgs.current = args;

    if (timerId) {
      return;
    }

    timerId = setTimeout(() => {
      callback(...lastArgs.current);

      timerId = null;
    }, wait);
  };
};
