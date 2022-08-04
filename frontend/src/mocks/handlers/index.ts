import commonHandler from './commonHandler';
import meetingHandler from './meetingHandler';
import userHandler from './userHandler';

const handlers = [...commonHandler, ...meetingHandler, ...userHandler];

export default handlers;
