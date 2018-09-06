/* eslint-disable no-param-reassign */

import {
  REQUEST_COURSE_SEARCH,
  RECEIVE_COURSE_SEARCH,
  ADD_TO_SCHEDULE,
  RESET_SCHEDULE,
  REMOVE_COURSE,
} from '../actions/types';

const initialState = {
  isFetching: false,
  courseList: [],
  searchList: {},
};

export default (state = initialState, action) => {
  switch (action.type) {
    case REQUEST_COURSE_SEARCH:
      return {
        ...state,
        isFetching: true,
      };
    case RECEIVE_COURSE_SEARCH:
      return {
        ...state,
        isFetching: false,
        searchList: action.error ? {} : action.payload,
      };
    case ADD_TO_SCHEDULE: {
      if (state.courseList.length === 12) {
        return state;
      }
      const { courses, id } = action.payload;
      const courseToAdd = [...courses];
      courseToAdd.id = id;
      const courseList = [...state.courseList, courseToAdd];
      return {
        ...state,
        courseList,
      };
    }
    case RESET_SCHEDULE:
      return {
        ...state,
        searchList: {},
        courseList: [],
      };
    case REMOVE_COURSE: {
      const courseList = [...state.courseList];
      courseList.splice(courseList.indexOf(action.payload), 1);
      return {
        ...state,
        courseList,
      };
    }
    default:
      return state;
  }
};
