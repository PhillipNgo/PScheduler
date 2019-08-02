import {
  START_FIRST_RENDER,
  START_GENERATING,
  END_GENERATING,
  FILTERED_COURSES,
  START_REDIRECT,
  END_REDIRECT,
  SELECT_SORT,
} from '../actions/types';

const initialState = {
  isGenerating: false,
  schedules: [],
  filteredCourses: [],
  redirect: null,
  initialValues: null,
  error: null,
  sort: false,
};

export default (state = initialState, action) => {
  switch (action.type) {
    case START_FIRST_RENDER:
      return {
        ...state,
        initialValues: action.payload,
      };
    case START_GENERATING:
      return {
        ...state,
        error: null,
        isGenerating: true,
      };
    case END_GENERATING:
      return {
        ...state,
        isGenerating: false,
        error: action.error,
        schedules: action.error ? [] : action.payload,
      };
    case FILTERED_COURSES:
      return {
        ...state,
        filteredCourses: action.payload,
      };
    case START_REDIRECT:
      return {
        ...state,
        redirect: action.payload,
      };
    case END_REDIRECT:
      return {
        ...state,
        redirect: null,
      };
    case SELECT_SORT:
      return {
        ...state,
        sort: action.payload,
      };
    default:
      return state;
  }
};
