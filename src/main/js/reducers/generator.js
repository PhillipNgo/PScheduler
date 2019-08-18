import {
  START_FIRST_RENDER,
  START_GENERATING,
  END_GENERATING,
  FILTERED_COURSES,
  START_REDIRECT,
  END_REDIRECT,
  SELECT_SORT,
  GENERATE_MORE,
} from '../actions/types';

const initialState = {
  isGenerating: false,
  schedules: [],
  filteredCourses: [],
  redirect: null,
  initialValues: null,
  error: null,
  sort: false,
  useCourseAvg: false,
  scheduleMaker: null,
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
        schedules: action.error ? [] : action.payload.schedules,
        scheduleMaker: action.error ? null : action.payload.scheduleMaker,
      };
    case GENERATE_MORE: {
      const { scheduleMaker } = state;

      if (!scheduleMaker) {
        return {
          ...state,
          isGenerating: false,
        };
      }

      let schedules;
      let error = null;
      try {
        schedules = [
          ...state.schedules,
          ...scheduleMaker.makeSchedules(),
        ];
      } catch (e) {
        error = e;
      }

      return {
        ...state,
        isGenerating: false,
        error,
        schedules: error ? state.schedules : schedules,
      };
    }
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
        sort: action.payload.sort,
        useCourseAvg: action.payload.useCourseAvg,
      };
    default:
      return state;
  }
};
