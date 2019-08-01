import { stringify } from 'query-string';
import filterCourses from './filter';
import {
  startGenerating,
  endGenerating,
  filteredCourses,
  startRedirect,
} from '../actions/generator';
import getGPAMap, { getInstructorLastName } from './grade';
import ScheduleMaker from './ScheduleMaker';
import 'whatwg-fetch';
import { shortenUrl } from '../constants/resources';
import { addGrades, sortByGPA } from '../actions/grades';

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
  if (courseList.length !== 0) {
    dispatch(startGenerating());
    dispatch(sortByGPA(values.sortByGPA));


    const shortCode = loadQuery || !values.genURL ? Promise.resolve(loadQuery.substring(2))
      : fetchShortUrl(courseList, values);

    const gpasLoading = values.sortByGPA ? getGPAMap(courseList) : Promise.reject();

    const filteredResults = filterCourses(values, courseList);
    dispatch(filteredCourses(filteredResults));
    const filteredCourseList = filteredResults.map(result => result.filtered);

    gpasLoading
      .then((gradeMap) => {
        dispatch(addGrades(gradeMap));
        filteredCourseList.forEach(list => list.sort((course1, course2) => {
          const name = `${course1.subject}${course1.courseNumber}`;
          const instructor1 = getInstructorLastName(course1.instructor); // Backend only holds instructors' last name
          const instructor2 = getInstructorLastName(course2.instructor);

          /** Can't sort if the backend doesn't hold data for the entire course */
          if (!gradeMap[name]) return 0;

          /** Use mapping to determine sort order */
          if (!gradeMap[name][instructor1] && !gradeMap[name][instructor2]) return 0;
          else if (!gradeMap[name][instructor1]) return 1;
          else if (!gradeMap[name][instructor2]) return -1;
          else {
            return gradeMap[name][instructor2] - gradeMap[name][instructor1];
          }
        }));
      })
      .finally(() => {
        const schedules = genSchedules(filteredCourseList, values.gap);

        if (values.sortByGPA) {
          const gradeMap = getState().grades.map;
          /** My Implementation of Insertion sort */
          for (let x = 1; x < schedules.length; x += 1) {
            const key = schedules[x];
            let j = x - 1;

            while (j >= 0 && schedules[j].calculateGPA(gradeMap) < key.calculateGPA(gradeMap)) {
              schedules[j + 1] = schedules[j];
              j -= 1;
            }
            schedules[j + 1] = key;
          }
        }

        shortCode
          .then(code => dispatch(redirectToGenerator(schedules, code ? `q=${code}` : '')))
          .catch(() => dispatch(redirectToGenerator(schedules)))
          .then(() => dispatch(endGenerating(schedules)));
      });
  }
};

export default generateSchedules;
