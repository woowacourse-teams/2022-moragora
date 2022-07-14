type Meeting = {
  id: number;
  name: string;
  startDate: string;
  endDate: string;
  entranceTime: string;
  leaveTime: string;
  userIds: number[];
};

const generateIndexes = (length: number) =>
  Array.from({ length }).map((_, id) => id);

const meetings = generateIndexes(100).map<Meeting>((id) => ({
  id,
  name: `모임${id}`,
  startDate: '2021-11-09',
  endDate: '2022-11-09',
  entranceTime: '10:00',
  leaveTime: '18:00',
  userIds: generateIndexes(100).splice(id, 5),
}));

export default meetings;
