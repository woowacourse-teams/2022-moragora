import { Meeting } from 'types/meetingType';

const generateIndexes = (length: number) =>
  Array.from({ length }).map((_, id) => id);

const meetings = generateIndexes(100).map<Meeting>((id) => ({
  id,
  name: `모임${id}`,
  isActive: true,
  userIds: generateIndexes(100).splice(id, 5),
  attendanceCount: 10,
}));

export default meetings;
