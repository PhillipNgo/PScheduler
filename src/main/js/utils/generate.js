import { stringify } from 'query-string';
import filterCourses from './filter';
import {
  startGenerating,
  endGenerating,
  filteredCourses,
  startRedirect,
} from '../actions/generator';
import addGPA from './Grade';
import ScheduleMaker from './ScheduleMaker';
import 'whatwg-fetch';
import { shortenUrl } from '../constants/resources';

const generateSchedules = (values, loadQuery) => (dispatch, getState) => {
  const { courseList } = getState().courses;
  if (courseList.length !== 0) {
    dispatch(startGenerating());
    addGPA(courseList).then((res) => {
      const filteredResults = filterCourses(values, res);
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
        if (values.sortByGPA === 'yes') {
          schedules.sort((schedule1, schedule2) => schedule2.gpa - schedule1.gpa);
        }
      }
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
      (loadQuery ? Promise.resolve() : fetch(`${shortenUrl}?${stringify({ data: JSON.stringify(queryObject), prefix: 'generator' })}`))
        .then(response => (loadQuery ? loadQuery.substring(2) : response.json()))
        .then((result) => {
          const querystring = `q=${result}`;
          dispatch(startRedirect({
            pathname: '/generator',
            hash: schedules.length > 0 ? '#schedules' : '#timetable',
            search: result ? querystring : '',
          }));
        })
        .catch(() => {
          dispatch(startRedirect({
            pathname: '/generator',
            hash: schedules.length > 0 ? '#schedules' : '#timetable',
            search: '',
          }));
        })
        .then(() => {
          dispatch(endGenerating(schedules));
        });
    });
  }
};

export default generateSchedules;
