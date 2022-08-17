import commonHandler from './commonHandler';
import meetingHandler from './meetingHandler';
import eventHandler from './eventHandler';
import userHandler from './userHandler';
import attendanceHandler from './attendanceHandler';

const handlers = [
  ...commonHandler,
  ...meetingHandler,
  ...eventHandler,
  ...userHandler,
  ...attendanceHandler,
];

export default handlers;
