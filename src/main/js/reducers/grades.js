import {
  ADD_COURSEGPA, SELECT_SORTBYGPA,
} from '../actions/types';

const initialState = {
  map: {},
  sort: false,
};

export default (state = initialState, action) => {
  switch (action.type) {
    case ADD_COURSEGPA:
      return {
        ...state,
        map: action.gpa,
      };
    case SELECT_SORTBYGPA:
      return {
        ...state,
        sort: action.sort,
      };
    default:
      return state;
  }
};
