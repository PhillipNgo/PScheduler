import {
  REQUEST_TIMETABLE_SEARCH,
  RECEIVE_TIMETABLE_SEARCH,
} from '../actions/types';

const initialState = {
  isFetching: false,
  searchList: {},
};

export default (state = initialState, action) => {
  switch (action.type) {
    case REQUEST_TIMETABLE_SEARCH:
      return {
        ...state,
        isFetching: true,
      };
    case RECEIVE_TIMETABLE_SEARCH:
      return {
        ...state,
        isFetching: false,
        searchList: action.error ? {} : action.payload,
      };
    default:
      return state;
  }
};
