import { RestRequest } from 'msw';
import { TOKEN_PREFIX } from 'mocks/configs';

const extractIdFromToken = (token: string) =>
  Number(token.replace(TOKEN_PREFIX, ''));

export const generateToken = (id: number) => `${TOKEN_PREFIX}${id}`;

export const extractIdFromHeader = <T>(
  req: RestRequest<T>
): {
  id?: number;
  isValidToken: boolean;
} => {
  const authorization = req.headers.get('Authorization');
  const accessToken = authorization?.trim().replace('Bearer', '');

  if (!accessToken) {
    return {
      isValidToken: false,
    };
  }

  return {
    id: extractIdFromToken(accessToken),
    isValidToken: true,
  };
};
