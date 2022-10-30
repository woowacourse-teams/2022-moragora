import axios from 'axios';

export const publicRequest = axios.create({
  baseURL: process.env.API_SERVER_HOST,
  headers: {
    'Content-Type': 'application/json',
    Authorization: 'public default',
  },
});

export const privateRequest = axios.create({
  baseURL: process.env.API_SERVER_HOST,
  headers: {
    'Content-Type': 'application/json',
    Authorization: 'private default',
  },
});
