import {
  REQUEST_BUILDER_SEARCH,
  RECEIVE_BUILDER_SEARCH,
  ADD_TO_BUILDER,
  REMOVE_FROM_BUILDER,
  SELECT_CRN,
  UNSELECT_CRN,
  RESET_BUILDER,
} from './types';

export const requestBuilderSearch = () => ({
  type: REQUEST_BUILDER_SEARCH,
});

export const receiveBuilderSearch = courses => ({
  type: RECEIVE_BUILDER_SEARCH,
  payload: courses,
  error: courses instanceof Error,
});

export const addToBuilder = (courses, id, course) => ({
  type: ADD_TO_BUILDER,
  payload: {
    courses,
    id,
    course,
  },
});

export const removeFromBuilder = courseName => ({
  type: REMOVE_FROM_BUILDER,
  payload: courseName,
});

export const selectCrn = (courseName, id, crn) => ({
  type: SELECT_CRN,
  payload: {
    courseName,
    id,
    crn,
  },
});

export const unselectCrn = (subject, courseNumber, id) => ({
  type: UNSELECT_CRN,
  payload: {
    subject,
    courseNumber,
    id,
  },
});

export const resetBuilder = () => ({
  type: RESET_BUILDER,
});
