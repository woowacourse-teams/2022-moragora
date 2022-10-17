export function debounce<T extends (...args: any[]) => unknown>(
  callback: T,
  delay: number
) {
  let timer: NodeJS.Timeout;

  return function (...args: Parameters<T>) {
    clearTimeout(timer);

    timer = setTimeout(() => {
      callback(...args);
    }, delay);
  };
}
