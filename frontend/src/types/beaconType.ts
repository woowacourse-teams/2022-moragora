export type Beacon = {
  id: number;
  meetingId: number;
  latitude: number;
  longitude: number;
  radius: number;
  address: string;
};

export type CreateBeaconsRequestBody = {
  beacons: Omit<Beacon, 'id' | 'meetingId'>[];
};
