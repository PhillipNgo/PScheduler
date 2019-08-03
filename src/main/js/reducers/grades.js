import {
  ADD_GRADES,
} from '../actions/types';

const initialState = {
  map: {},
};

export default (state = initialState, action) => {
  switch (action.type) {
    case ADD_GRADES: {
      const map = action.error ? {} : action.payload;
      return {
        ...state,
        map,
        error: action.error,
      };
    }
    default:
      return state;
  }
};
