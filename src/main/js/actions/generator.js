import {
  START_GENERATING,
  END_GENERATING,
  FILTERED_COURSES,
  START_REDIRECT,
  END_REDIRECT,
} from './types';

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
