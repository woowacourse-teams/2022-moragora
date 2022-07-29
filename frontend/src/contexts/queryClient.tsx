import { useContext, createContext } from 'react';

type QueryClient = {
  cache: Record<string, any>;
  invalidateQueries: (key: string) => void;
  isCached: (key: string) => boolean;
};

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
