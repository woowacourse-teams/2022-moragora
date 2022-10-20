import request from 'utils/request';
import { CreateBeaconsRequestBody } from 'types/beaconType';

export const createBeaconsApi =
  () =>
  ({
    meetingId,
    ...payload
  }: CreateBeaconsRequestBody & { meetingId: number }) =>
    request<{}>(`/meetings/${meetingId}/beacons`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(payload),
    });
