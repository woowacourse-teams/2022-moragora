import { RestRequest } from 'msw';
import { TOKEN_PREFIX } from 'mocks/configs';

const extractIdFromToken = (token: string) => Number(token.split('_')[1]);

const extractExpiredTimestampFromAccessToken = (token: string) =>
  Number(token.split('_'.slice(-1)));

const extractIdFromRefreshToken = (token: string) =>
  Number(token.split('_')[2]);

const extractExpiredTimestampFromRefreshToken = (token: string) =>
  Number(token.split('_'.slice(-1)));

export const generateAccessToken = (id: number) => {
  const timer = new Date();
  timer.setMinutes(timer.getMinutes() + 5);
  const expiredTimestamp = timer.getTime();

  return `${TOKEN_PREFIX}_${id}_${expiredTimestamp}`;
};

export const generateRefreshToken = (id: number) => {
  const timer = new Date();
  timer.setDate(timer.getDate() + 8);
  const expiredTimestamp = timer.getTime();

  return `${TOKEN_PREFIX}_refresh_${id}_${expiredTimestamp}`;
};

export const extractIdFromHeader = (
  req: RestRequest
): {
  id?: number;
  expiredTimestamp?: number;
  isValidToken: boolean;
} => {
  const authorization = req.headers.get('Authorization');
  const accessToken = authorization?.trim().replace('Bearer', '');
  const isValidToken = /\w_\d_\d/.test(accessToken || '');

  if (!accessToken) {
    return {
      isValidToken,
    };
  }

  return {
    id: extractIdFromToken(accessToken),
    expiredTimestamp: extractExpiredTimestampFromAccessToken(accessToken),
    isValidToken,
  };
};

export const extractIdFromCookie = (
  req: RestRequest
): {
  id?: number;
  expiredTimestamp?: number;
  isValidToken?: boolean;
  isExist: boolean;
} => {
  const { refreshToken } = req.cookies;
  const isValidToken = /\w_refresh_\d_\d/.test(refreshToken);

  if (!refreshToken) {
    return {
      isExist: false,
    };
  }

  return {
    id: extractIdFromRefreshToken(refreshToken),
    expiredTimestamp: extractExpiredTimestampFromRefreshToken(refreshToken),
    isExist: true,
    isValidToken,
  };
};

export const checkExpiredToken = (expiredTimestamp?: number) => {
  if (!expiredTimestamp) {
    return false;
  }

  expiredTimestamp <= Date.now();
};
