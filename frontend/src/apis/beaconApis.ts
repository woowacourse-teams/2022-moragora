import request from 'utils/request';
import { User } from 'types/userType';
import { CreateBeaconsRequestBody } from 'types/beaconType';

export const createBeaconsApi =
  ({ accessToken }: { accessToken?: User['accessToken'] }) =>
  ({
    meetingId,
    ...payload
  }: CreateBeaconsRequestBody & { meetingId: number }) =>
    request<{}>(`/meetings/${meetingId}/beacons`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${accessToken}`,
      },
      body: JSON.stringify(payload),
    });
