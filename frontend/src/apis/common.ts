import request from 'utils/request';

export const getServerTime = () =>
  request<{ serverTime: number }>('/server-time', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  });
