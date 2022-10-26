import { useContext, createContext } from 'react';
import { QueryClient } from 'types/queryType';

export const queryClient: QueryClient = {
  cache: {},
  invalidateQueries: function (key) {
    delete this.cache[key];
  },
  isCached: (key) =>
    Object.prototype.hasOwnProperty.call(queryClient.cache, key),
};

const QueryClientContext = createContext(queryClient);
export const useQueryClient = () => useContext(QueryClientContext);
export const QueryClientProvider = QueryClientContext.Provider;
