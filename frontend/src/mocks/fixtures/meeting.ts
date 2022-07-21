import { Meeting } from 'types/meetingType';

const generateIndexes = (length: number) =>
  Array.from({ length }).map((_, id) => id);

const meetings = generateIndexes(100).map<Meeting>((id) => ({
  id,
  name: `모임${id}`,
  isActive: true,
  startDate: '2021-11-09',
  endDate: '2022-11-09',
  entranceTime: '10:00',
  leaveTime: '18:00',
  closingTime: '10:05',
  userIds: generateIndexes(100).splice(id, 5),
  attendanceCount: 10,
}));

export default meetings;
