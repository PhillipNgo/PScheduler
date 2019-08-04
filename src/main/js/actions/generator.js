import {
  START_FIRST_RENDER,
  START_GENERATING,
  END_GENERATING,
  FILTERED_COURSES,
  START_REDIRECT,
  END_REDIRECT,
  SELECT_SORT,
} from './types';

export const startFirstRender = formValues => ({
  type: START_FIRST_RENDER,
  payload: formValues,
});

export const startGenerating = () => ({
  type: START_GENERATING,
});

export const endGenerating = schedules => ({
  type: END_GENERATING,
  payload: schedules,
  error: schedules instanceof Error,
});

export const filteredCourses = courses => ({
  type: FILTERED_COURSES,
  payload: courses,
});

export const startRedirect = location => ({
  type: START_REDIRECT,
  payload: location,
});

export const endRedirect = () => ({
  type: END_REDIRECT,
});

export const selectSort = sort => ({
  type: SELECT_SORT,
  payload: sort,
});
