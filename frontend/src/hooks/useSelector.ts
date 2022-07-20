import React, { useContext } from 'react';

const useSelector = <ContextValues>(
  context: React.Context<ContextValues | null>
) => {
  const values = useContext(context);

  if (!values) {
    throw new Error('데이터가 없습니다.');
  }

  return values;
};

export default useSelector;
