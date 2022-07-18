import { useCallback, useState } from 'react';

const throttle = <T extends (...args: any) => any>(
  callback: T,
  wait: number = 0
) => {
  const time = Date.now();
  let timerId: NodeJS.Timeout | null = null;
  let lastArgs: {
    current: Parameters<T>;
  } = {
    current: null,
  };

  return (...args: Parameters<T>) =>
    new Promise<ReturnType<T>>((resolve) => {
      lastArgs.current = args;

      if (timerId) {
        return;
      }

      timerId = setTimeout(() => {
        resolve(callback(...lastArgs.current));

        timerId = null;
      }, wait);
    });
};

const useQuerySelectItems = <T extends { id: number }>(
  url: string,
  options: Partial<{
    wait: number;
    onError: (e: Error) => void;
    onSuccessful: (res: Response) => void;
  }>
) => {
  const [queryResult, setQueryResult] = useState<T[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedItems, setSelectedItems] = useState<T[]>([]);
  const filteredQueryResult = queryResult.filter(
    (item) => !selectedItems.find((selectedItem) => selectedItem.id === item.id)
  );

  const queryWithKeyword = async (keyword: string) => {
    setLoading(true);

    try {
      const res = await fetch(`${url}${keyword}`);

      if (!res.ok) {
        throw new Error('회원 조회를 실패했습니다.');
      }

      const users = await res.json().then((body: { users: T[] }) => body.users);

      setQueryResult(users);

      if (options.onSuccessful) {
        options.onSuccessful(res);
      }
    } catch (e) {
      if (options.onError) {
        options.onError(e);
      }
    } finally {
      setLoading(false);
    }
  };

  const optimizedQueryWithKeyword = useCallback(
    throttle(queryWithKeyword, options.wait),
    []
  );

  const selectItem = (item: T) => {
    setSelectedItems((prev) => [...prev, item]);
  };

  const unselectItem = (id: T['id']) => {
    setSelectedItems((prev) =>
      prev.filter((selectedItem) => selectedItem.id !== id)
    );
  };

  const clearQueryResult = () => {
    setQueryResult([]);
  };

  return {
    loading,
    queryResult: filteredQueryResult,
    selectedItems,
    queryWithKeyword: optimizedQueryWithKeyword,
    selectItem,
    unselectItem,
    clearQueryResult,
  };
};

export default useQuerySelectItems;
