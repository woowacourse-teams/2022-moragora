import { useContext, createContext } from 'react';
import { QueryClient } from 'types/queryType';

export const queryClient: QueryClient = {
  dataCache: {},
  queryCache: null,
  invalidateQueries: function (key) {
    delete this.dataCache[key];
  },
  isCached: (key) =>
    Object.prototype.hasOwnProperty.call(queryClient.dataCache, key),
  clearQueryCache: function () {
    this.queryCache = null;
  },
  reQueryCache: function () {
    if (this.queryCache) {
      this.queryCache();
      this.clearQueryCache();
    }
  },
  setQueryCache: function (query: () => Promise<void>) {
    this.queryCache = query;
  },
};

const QueryClientContext = createContext(queryClient);
export const useQueryClient = () => useContext(QueryClientContext);
export const QueryClientProvider = QueryClientContext.Provider;
