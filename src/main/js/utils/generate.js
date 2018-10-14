import { stringify } from 'query-string';
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
  const queryObject = {
    ...values,
    c: courseList.map((course) => {
      const name = `${course[0].subject}${course[0].courseNumber}`;
      return JSON.stringify({
        name,
        ...(values.courses ? values.courses[`${name}${course.id}`] : {}),
      });
    }),
  };
  delete queryObject.courses;
  const querystring = stringify(queryObject, { arrayFormat: 'bracket' });
  if (courseList.length !== 0) {
    dispatch(startGenerating());
    const filteredResults = filterCourses(values, courseList);
    dispatch(filteredCourses(filteredResults));
    const filteredCourseList = filteredResults.map(result => (
      result.filtered));
    let schedules = [];
    if (!filteredCourseList.find(list => list.length === 0)) {
      const scheduleMaker = new ScheduleMaker(filteredCourseList, parseInt(values.gap, 10));
      try {
        schedules = scheduleMaker.makeSchedules();
      } catch (e) {
        schedules = e;
      }
    }
    if (schedules.length > 0) {
      dispatch(startRedirect({
        pathname: '/generator',
        hash: '#schedules',
        search: querystring,
      }));
    } else {
      dispatch(startRedirect({
        pathname: '/generator',
        hash: '#timetable',
        search: querystring,
      }));
    }
    dispatch(endGenerating(schedules));
  }
};

export default generateSchedules;
