import React, { useContext } from 'react';

const useContextValues = <ContextValues>(
  context: React.Context<ContextValues | null>
) => {
  const values = useContext(context);

  if (!values) {
    throw new Error('Not Exist Context');
  }

  return values;
};

export default useContextValues;
