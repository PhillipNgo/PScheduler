import {
  ADD_COURSEGPA, CLEAR_GRADES,
} from '../actions/types';

export default (state = {}, action) => {
  switch (action.type) {
    case ADD_COURSEGPA: {
      return action.payload.gpa;
    }
    case CLEAR_GRADES:
      return {};
    default:
      return state;
  }
};
