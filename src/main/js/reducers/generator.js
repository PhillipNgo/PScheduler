import {
  START_GENERATING,
  END_GENERATING,
  FILTERED_COURSES,
  START_REDIRECT,
  END_REDIRECT,
} from '../actions/types';

const initialState = {
  isGenerating: false,
  schedules: [],
  filteredCourses: [],
  redirect: null,
};

export default (state = initialState, action) => {
  switch (action.type) {
    case START_GENERATING:
      return {
        ...state,
        isGenerating: true,
      };
    case END_GENERATING:
      return {
        ...state,
        isGenerating: false,
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
    default:
      return state;
  }
};
