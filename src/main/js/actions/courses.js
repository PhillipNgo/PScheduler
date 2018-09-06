import {
  REQUEST_COURSE_SEARCH,
  RECEIVE_COURSE_SEARCH,
  ADD_TO_SCHEDULE,
  RESET_SCHEDULE,
  REMOVE_COURSE,
} from './types';

export const requestCourseSearch = () => ({
  type: REQUEST_COURSE_SEARCH,
});

export const receiveCourseSearch = courses => ({
  type: RECEIVE_COURSE_SEARCH,
  payload: courses,
  error: courses instanceof Error,
});

export const addToSchedule = (courses, id) => ({
  type: ADD_TO_SCHEDULE,
  payload: {
    courses,
    id,
  },
});

export const resetSchedule = () => ({
  type: RESET_SCHEDULE,
});

export const removeCourse = course => ({
  type: REMOVE_COURSE,
  payload: course,
});
