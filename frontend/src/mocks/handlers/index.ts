import commonHandler from './commonHandler';
import meetingHandler from './meetingHandler';
import eventHandler from './eventHandler';
import userHandler from './userHandler';
import attendanceHandler from './attendanceHandler';
import beaconHandler from './beaconHandler';

const handlers = [
  ...commonHandler,
  ...meetingHandler,
  ...eventHandler,
  ...userHandler,
  ...attendanceHandler,
  ...beaconHandler,
];

export default handlers;
