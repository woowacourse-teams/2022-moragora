import { CreateBeaconsRequestBody } from 'types/beaconType';
import { privateRequest } from './instances';

export const createBeaconsApi =
  () =>
  ({
    meetingId,
    ...payload
  }: CreateBeaconsRequestBody & { meetingId: number }) =>
    privateRequest.post<{}>(`/meetings/${meetingId}/beacons`, payload);
