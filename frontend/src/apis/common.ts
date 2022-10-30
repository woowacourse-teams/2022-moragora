import { privateRequest } from './api';

export const getServerTime = () =>
  privateRequest.get<{ serverTime: number }>('/server-time');
