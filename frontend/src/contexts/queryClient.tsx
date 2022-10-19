import { useContext, createContext } from 'react';
import { QueryClient } from 'types/queryType';

export const queryClient: QueryClient = {
  dataCache: {},
  queryCache: [],
  invalidateQueries: function (key) {
    delete this.dataCache[key];
  },
  isCached: (key) =>
    Object.prototype.hasOwnProperty.call(queryClient.dataCache, key),
  clearQueryCache: function () {
    this.queryCache = [];
  },
  reQueryAllCache: function () {
    this.queryCache.forEach(async (query) => await query());
  },
  addQueryCache: function (query: () => Promise<void>) {
    this.queryCache.push(query);
  },
};

const QueryClientContext = createContext(queryClient);
export const useQueryClient = () => useContext(QueryClientContext);
export const QueryClientProvider = QueryClientContext.Provider;
