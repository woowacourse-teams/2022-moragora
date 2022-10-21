import commonHandler from './commonHandler';
import meetingHandler from './meetingHandler';
import eventHandler from './eventHandler';
import userHandler from './userHandler';
import attendanceHandler from './attendanceHandler';
import beaconHandler from './beaconHandler';
import tokenHandler from './tokenHandler';

const handlers = [
  ...commonHandler,
  ...meetingHandler,
  ...eventHandler,
  ...userHandler,
  ...attendanceHandler,
  ...beaconHandler,
  ...tokenHandler,
];

export default handlers;
