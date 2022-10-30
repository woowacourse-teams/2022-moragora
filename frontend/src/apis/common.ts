import { privateRequest } from './instances';

export const getServerTime = () =>
  privateRequest.get<{ serverTime: number }>('/server-time');
