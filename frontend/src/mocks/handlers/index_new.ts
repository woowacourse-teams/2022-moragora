import commonHandler from './commonHandler';
import meetingHandler_new from './meetingHandler_new';
import eventHandler_new from './eventHandler_new';
import userHandler from './userHandler';
import attendanceHandler_new from './attendanceHandler_new';

const handlers = [
  ...commonHandler,
  ...meetingHandler_new,
  ...eventHandler_new,
  ...userHandler,
  ...attendanceHandler_new,
];

export default handlers;
