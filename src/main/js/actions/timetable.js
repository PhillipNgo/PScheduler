import {
  REQUEST_TIMETABLE_SEARCH,
  RECEIVE_TIMETABLE_SEARCH,
} from './types';

export const requestTimetableSearch = () => ({
  type: REQUEST_TIMETABLE_SEARCH,
});

export const receiveTimetableSearch = courses => ({
  type: RECEIVE_TIMETABLE_SEARCH,
  payload: courses,
  error: courses instanceof Error,
});
