import {
  ADD_GRADES,
} from './types';

export const addGrades = gpa => ({
  type: ADD_GRADES,
  payload: gpa,
  error: gpa instanceof Error,
});

export default addGrades;
