import { rest } from 'msw';

export const handlers = [rest.get('/', null)];
