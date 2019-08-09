import { stringify } from 'query-string';
import filterCourses from './filter';
import {
  startGenerating,
  endGenerating,
  filteredCourses,
  startRedirect,
  selectSort,
} from '../actions/generator';
import getGPAMap, { getInstructorLastName } from './grade';
import ScheduleMaker from './ScheduleMaker';
import 'whatwg-fetch';
import { shortenUrl } from '../constants/resources';
import { addGrades } from '../actions/grades';

const genSchedules = (courseList, gap) => {
  let schedules = [];
  if (!courseList.find(list => list.length === 0)) {
    const scheduleMaker = new ScheduleMaker(courseList, parseInt(gap, 10));
    try {
      schedules = scheduleMaker.makeSchedules();
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

const generateSchedules = (values, loadQuery = '') => (dispatch, getState) => {
  const { courseList } = getState().courses;
  const {
    sortByGPA,
    useCourseAvg,
    genURL,
    gap,
  } = values;
  if (courseList.length !== 0) {
    dispatch(startGenerating());
    dispatch(selectSort(sortByGPA, useCourseAvg));

    const shortCode = loadQuery || !genURL ? Promise.resolve(loadQuery.substring(2))
      : fetchShortUrl(courseList, values);

    const gpasLoading = sortByGPA ? getGPAMap(courseList) : Promise.reject();

    const filteredResults = filterCourses(values, courseList);
    dispatch(filteredCourses(filteredResults));
    const filteredCourseList = filteredResults.map(result => result.filtered);

    gpasLoading
      .then((gradeMap) => {
        dispatch(addGrades(gradeMap));
        filteredCourseList.forEach(list => list.sort((course1, course2) => {
          const name = `${course1.subject}${course1.courseNumber}`;

          // Backend only holds instructors' last name
          const instructor1 = getInstructorLastName(course1.instructor);
          const instructor2 = getInstructorLastName(course2.instructor);

          if (gradeMap[name]) {
            if (!gradeMap[name][instructor1] && !gradeMap[name][instructor2]) return 0;
            if (!gradeMap[name][instructor1]) {
              return useCourseAvg ? gradeMap[name][instructor2] - gradeMap[name].AVERAGE : 1;
            }
            if (!gradeMap[name][instructor2]) {
              return useCourseAvg ? gradeMap[name].AVERAGE - gradeMap[name][instructor1] : -1;
            }
          }

          return 0;
        }));
      }, () => Promise.resolve())
      .catch(error => dispatch(addGrades(error)))
      .finally(() => {
        const schedules = genSchedules(filteredCourseList, gap);

        if (!(schedules instanceof Error) && sortByGPA) {
          const gradeMap = getState().grades.map;
          schedules.sort((schedule1, schedule2) => schedule2.calculateGPA(gradeMap, useCourseAvg)
            - schedule1.calculateGPA(gradeMap, useCourseAvg));
        }

        shortCode
          .then(code => dispatch(redirectToGenerator(schedules, code ? `q=${code}` : '')))
          .catch(() => dispatch(redirectToGenerator(schedules)))
          .then(() => dispatch(endGenerating(schedules)));
      });
  }
};

export default generateSchedules;
