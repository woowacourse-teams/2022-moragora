import { rest } from 'msw';

const handlers = [rest.get('/', null)];

export default handlers;
