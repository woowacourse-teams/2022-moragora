import commonHandler from './commonHandler';
import meetingHandler from './meetingHandler';
import eventHandler from './eventHandler';
import userHandler from './userHandler';

const handlers = [
  ...commonHandler,
  ...meetingHandler,
  ...eventHandler,
  ...userHandler,
];

export default handlers;
