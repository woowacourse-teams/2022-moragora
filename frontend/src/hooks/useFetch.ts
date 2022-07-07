import { useState, useEffect } from 'react';

const useFetch = <ResponseBodyType = any>(url: string) => {
  const [data, setData] = useState<ResponseBodyType>();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [fetchingCount, setFetchingCount] = useState(0);

  const refetch = () => {
    setFetchingCount((prev) => prev + 1);
  };

  useEffect(() => {
    setLoading(true);

    fetch(url)
      .then((res) => res.json())
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
