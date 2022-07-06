import { useState, useEffect } from 'react';

const useFetch = <ResponseBodyType = any>(url: string) => {
  const [data, setData] = useState<ResponseBodyType>();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

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
  }, []);

  return {
    data,
    loading,
    error,
  };
};

export default useFetch;
