import filterCourses from './filter';
import {
  startGenerating,
  endGenerating,
  filteredCourses,
  startRedirect,
} from '../actions/generator';
import ScheduleMaker from './ScheduleMaker';

const generateSchedules = values => (dispatch, getState) => {
  const { courseList } = getState().courses;
  if (courseList.length !== 0) {
    dispatch(startRedirect({ pathname: '/generator', hash: '#schedules' }));
    dispatch(startGenerating());
    const filteredResults = filterCourses(values, courseList);
    dispatch(filteredCourses(filteredResults));
    const filteredCourseList = filteredResults.map(result => (
      result.filtered));
    if (!filteredCourseList.find(list => list.length === 0)) {
      const scheduleMaker = new ScheduleMaker(filteredCourseList, parseInt(values.gap, 10));
      try {
        const schedules = scheduleMaker.makeSchedules();
        dispatch(endGenerating(schedules));
      } catch (e) {
        dispatch(endGenerating(e));
      }
    } else {
      dispatch(startRedirect({ pathname: '/generator', hash: '#timetable' }));
      dispatch(endGenerating([]));
    }
  }
};

export default generateSchedules;
