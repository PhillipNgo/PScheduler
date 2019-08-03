import {
  ADD_GRADES,
} from '../actions/types';

const initialState = {
  map: {},
};

export default (state = initialState, action) => {
  switch (action.type) {
    case ADD_GRADES:
      return {
        ...state,
        map: action.error ? {} : action.payload,
        error: action.error,
      };
    default:
      return state;
  }
};
