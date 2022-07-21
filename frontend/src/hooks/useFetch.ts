import { useState, useEffect, useContext } from 'react';
import { TOKEN_ERROR_STATUS_CODES } from 'consts';
import { userContext } from 'contexts/userContext';

const useFetch = <ResponseBodyType = any>(url: string) => {
  const [data, setData] = useState<ResponseBodyType>();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [fetchingCount, setFetchingCount] = useState(0);
  const userState = useContext(userContext);

  const refetch = () => {
    setFetchingCount((prev) => prev + 1);
  };

  useEffect(() => {
    setLoading(true);

    const accessToken = userState?.user?.accessToken
      ? userState?.user?.accessToken
      : localStorage.getItem('accessToken');

    fetch(`${process.env.API_SERVER_HOST}${url}`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
      .then((res) => {
        if (!res.ok) {
          if (TOKEN_ERROR_STATUS_CODES.includes(res.status)) {
            userState?.logout();
          }

          throw new Error('요청에 실패했습니다.');
        }

        return res.json();
      })
      .then((body) => {
        setData(body);
      })
      .catch((e) => {
        setError(e);
      })
      .finally(() => setLoading(false));
  }, [fetchingCount]);

  return {
    data,
    loading,
    error,
    fetchingCount,
    refetch,
  };
};

export default useFetch;
