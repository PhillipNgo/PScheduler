import {
  ADD_COURSEGPA,
  CLEAR_GRADES,
} from './types';

export const clearGrades = () => ({
  type: CLEAR_GRADES,
});

export const addGrades = gpa => ({
  type: ADD_COURSEGPA,
  payload: {
    gpa,
  },
});
