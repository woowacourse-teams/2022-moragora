import { AttendanceStatus } from 'types/userType';

export const TOKEN_ERROR_STATUS_CODES = [401, 404];

export const NOT_FOUND_STATUS_CODE = 404;

export const QUERY_STATUS = {
  LOADING: 'loading',
  ERROR: 'error',
  SUCCESS: 'success',
};

export const MUTATION_STATUS = {
  IDLE: 'idle',
  LOADING: 'loading',
  ERROR: 'error',
  SUCCESS: 'success',
};

export const ATTENDANCE_STATUS: Record<AttendanceStatus, boolean> = {
  tardy: false,
  present: true,
} as const;
