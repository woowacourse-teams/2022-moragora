import React, { useContext } from 'react';

const useContextValues = <ContextValues>(
  context: React.Context<ContextValues | null>
) => {
  const values = useContext(context);

  if (!values) {
    return;
  }

  return values;
};

export default useContextValues;
