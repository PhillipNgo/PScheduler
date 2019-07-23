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

const genSchedules = (courseList, gap, sort) => {
  let schedules = [];
  if (!courseList.find(list => list.length === 0)) {
    const scheduleMaker = new ScheduleMaker(courseList, parseInt(gap, 10));
    try {
      schedules = scheduleMaker.makeSchedules();
      if (sort === 'yes') {
        schedules.sort((schedule1, schedule2) => schedule2.gpa - schedule1.gpa);
      }
    } catch (e) {
      schedules = e;
    }
  }
  return schedules;
};

const fetchShortUrl = (courseList, formValues) => {
  const queryObject = {
    ...formValues,
    c: courseList.map((course) => {
      const name = `${course[0].subject}${course[0].courseNumber}`;
      return JSON.stringify({
        name,
        ...(formValues.courses ? formValues.courses[`${name}${course.id}`] : {}),
      });
    }),
  };
  delete queryObject.courses;
  return fetch(`${shortenUrl}?${stringify({ data: JSON.stringify(queryObject), prefix: 'generator' })}`)
    .then(response => response.json());
};

const redirectToGenerator = (schedules, search = '') => startRedirect({
  pathname: '/generator',
  hash: schedules.length > 0 ? '#schedules' : '#timetable',
  search: search.replace('==', '='),
});

const generateSchedules = (values, loadQuery) => (dispatch, getState) => {
  const { courseList } = getState().courses;
  if (courseList.length !== 0) {
    dispatch(startGenerating());

    const shortCode = loadQuery ? Promise.resolve(loadQuery.substring(2))
      : fetchShortUrl(courseList, values);

    addGPA(courseList).then((res) => {
      const filteredResults = filterCourses(values, res);
      dispatch(filteredCourses(filteredResults));
      const filteredCourseList = filteredResults.map(result => result.filtered);

      const schedules = genSchedules(filteredCourseList, values.gap, values.sortByGPA);

      shortCode
        .then(code => dispatch(redirectToGenerator(schedules, code ? `q=${code}` : '')))
        .catch(() => dispatch(redirectToGenerator(schedules)))
        .then(() => dispatch(endGenerating(schedules)));
    });
  }
};

export default generateSchedules;
