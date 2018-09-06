import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form';
import courses from './courses';
import generator from './generator';
import timetable from './timetable';
import builder from './builder';

export default combineReducers({
  courses,
  generator,
  timetable,
  builder,
  form: formReducer,
});
