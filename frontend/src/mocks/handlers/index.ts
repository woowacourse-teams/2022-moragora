import meetingHandler from './meetingHandler';
import userHandler from './userHandler';

const handlers = [...meetingHandler, ...userHandler];

export default handlers;
