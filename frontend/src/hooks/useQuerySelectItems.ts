import { useCallback, useContext, useState } from 'react';
import { throttle } from 'utils/throttle';
import { TOKEN_ERROR_STATUS_CODES } from 'consts';
import { userContext, UserContextValues } from 'contexts/userContext';

const useQuerySelectItems = <T extends { id: number }>(
  url: string,
  options: Partial<{
    wait: number;
    maxSelectCount: number;
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
  const userState = useContext(userContext) as UserContextValues;

  const queryWithKeyword = async (keyword: string) => {
    setLoading(true);

    try {
      const res = await fetch(
        `${process.env.API_SERVER_HOST}${url}${keyword}`,
        {
          headers: {
            Authorization: `Bearer ${userState.accessToken}`,
          },
        }
      );

      if (!res.ok) {
        if (TOKEN_ERROR_STATUS_CODES.includes(res.status)) {
          userState.logout();
        }

        throw new Error('회원 조회를 실패했습니다.');
      }

      const users = await res.json().then((body: { users: T[] }) => body.users);

      setQueryResult(users);

      if (options.onSuccessful) {
        options.onSuccessful(res);
      }
    } catch (e) {
      if (options.onError) {
        options.onError(e as Error);
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
    const isSelectedCountExceed =
      selectedItems.length >=
      (options.maxSelectCount ? options.maxSelectCount : 30);

    if (isSelectedCountExceed) {
      return;
    }

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
