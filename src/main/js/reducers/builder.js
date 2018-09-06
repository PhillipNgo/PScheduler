import {
  REQUEST_BUILDER_SEARCH,
  RECEIVE_BUILDER_SEARCH,
  ADD_TO_BUILDER,
  REMOVE_FROM_BUILDER,
  SELECT_CRN,
  UNSELECT_CRN,
  RESET_BUILDER,
} from '../actions/types';
import Schedule from '../utils/Schedule';

const initialState = {
  isFetching: false,
  courseList: [],
  searchList: {},
  schedule: new Schedule(0),
};

const createEmptyCourse = (subject, courseNumber) => ({
  crn: 'None',
  subject,
  courseNumber,
  title: '',
  type: '',
  credits: '',
  instructor: '',
  meetings: [{
    days: '',
    location: '',
    startTime: '',
    endTime: '',
  }],
});

export default (state = initialState, action) => {
  switch (action.type) {
    case REQUEST_BUILDER_SEARCH:
      return {
        ...state,
        isFetching: true,
      };
    case RECEIVE_BUILDER_SEARCH:
      return {
        ...state,
        isFetching: false,
        searchList: action.error ? {} : action.payload,
      };
    case ADD_TO_BUILDER: {
      if (state.courseList.length === 12) {
        return state;
      }
      const { courses, id, course } = action.payload;
      const newSchedule = new Schedule(state.schedule);
      const selected = course === null || newSchedule.conflictsWith(course)
        ? createEmptyCourse(courses[0].subject, courses[0].courseNumber) : course;
      newSchedule.add(selected);
      const courseList = [...state.courseList, {
        selected,
        id,
        courses,
      }];
      return {
        ...state,
        courseList,
        schedule: newSchedule,
      };
    }
    case REMOVE_FROM_BUILDER: {
      const newSchedule = new Schedule(state.schedule);
      const list = state.courseList.find(courses => courses === action.payload);
      const removed = newSchedule.delete(list.selected);
      const courseList = [...state.courseList];
      courseList.splice(courseList.indexOf(action.payload), 1);
      return {
        ...state,
        schedule: removed ? newSchedule : state.schedule,
        courseList,
      };
    }
    case SELECT_CRN: {
      const { courseName, id, crn } = action.payload;
      const list = state.courseList.find(course => `${course.courses[0].subject}${course.courses[0].courseNumber}` === courseName && course.id === id);
      const selected = list.courses.find(course => `${course.crn}` === crn);
      const newSchedule = new Schedule(state.schedule);
      const deleted = newSchedule.delete(list.selected);
      const added = newSchedule.add(selected);
      const courseList = [...state.courseList];
      courseList.splice(courseList.indexOf(list), 1, {
        selected,
        id: list.id,
        courses: list.courses,
      });
      return {
        ...state,
        schedule: added && deleted ? newSchedule : state.schedule,
        courseList: added && deleted ? courseList : state.courseList,
      };
    }
    case UNSELECT_CRN: {
      const { subject, courseNumber, id } = action.payload;
      const list = state.courseList.find(course => `${course.courses[0].subject}${course.courses[0].courseNumber}` === `${subject}${courseNumber}` && course.id === id);
      const newSchedule = new Schedule(state.schedule);
      const emptyCourse = createEmptyCourse(subject, courseNumber);
      const removed = newSchedule.delete(list.selected);
      newSchedule.add(emptyCourse);
      const courseList = [...state.courseList];
      courseList.splice(courseList.indexOf(list), 1, {
        selected: emptyCourse,
        id: list.id,
        courses: list.courses,
      });
      return {
        ...state,
        schedule: removed ? newSchedule : state.schedule,
        courseList: removed ? courseList : state.courseList,
      };
    }
    case RESET_BUILDER:
      return initialState;
    default:
      return state;
  }
};
