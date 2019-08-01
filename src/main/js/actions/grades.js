import {
  ADD_COURSEGPA,
  SELECT_SORTBYGPA,
} from './types';

export const addGrades = gpa => ({
  type: ADD_COURSEGPA,
  gpa,
});

export const sortByGPA = sort => ({
  type: SELECT_SORTBYGPA,
  sort,
});
