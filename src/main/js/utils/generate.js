import { stringify } from 'query-string';
import filterCourses from './filter';
import {
  startGenerating,
  endGenerating,
  filteredCourses,
  startRedirect,
} from '../actions/generator';
import addGPA from './grade';
import ScheduleMaker from './ScheduleMaker';
import 'whatwg-fetch';
import { shortenUrl } from '../constants/resources';
import { addGrades } from '../actions/grades';
import { getInstructorLastName, calculateGPA } from '../components/CourseTable';

const genSchedules = (courseList, gap, sort, gradeMap) => {
  let schedules = [];
  if (!courseList.find(list => list.length === 0)) {
    const scheduleMaker = new ScheduleMaker(courseList, parseInt(gap, 10), sort);
    try {
      schedules = scheduleMaker.makeSchedules();
      if (sort === 'yes') {
        /** My Implementation of Insertion sort */
        for (let x = 1; x < schedules.length; x += 1) {
          const key = schedules[x];
          let j = x - 1;

          while (j >= 0 && calculateGPA(schedules[j], gradeMap) < calculateGPA(key, gradeMap)) {
            schedules[j + 1] = schedules[j];
            j -= 1;
          }
          schedules[j + 1] = key;
        }
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

const generateSchedules = (values, loadQuery = '') => (dispatch, getState) => {
  const { courseList } = getState().courses;
  if (courseList.length !== 0) {
    dispatch(startGenerating());

    const shortCode = loadQuery || !values.genURL ? Promise.resolve(loadQuery.substring(2))
      : fetchShortUrl(courseList, values);

    const filteredResults = filterCourses(values, courseList);
    dispatch(filteredCourses(filteredResults));
    let filteredCourseList = filteredResults.map(result => result.filtered);

    // REMEMBER TO CHANGE BELOW SO THAT IT CHECKS THE STRING AND NOT FALSELY
    const gpasLoading = values.sortByGPA ? addGPA(filteredCourseList) : Promise.reject();

    gpasLoading
      .then((res) => {
        dispatch(addGrades(res));
        filteredCourseList = filteredCourseList.map(list => list.sort((course1, course2) => {
          const name = `${course1.subject}${course1.courseNumber}`;
          const instructor1 = getInstructorLastName(course1.instructor); // Backend only holds instructors' last name
          const instructor2 = getInstructorLastName(course2.instructor);

          /** Can't sort if the backend doesn't hold data for the entire course */
          if (!res[name]) return 0;

          /** Assume when instructor === 'Staff', then the course has a gpa of 0.00 */
          if (instructor1 === 'Staff' && instructor2 === 'Staff') return 0;
          else if (instructor1 === 'Staff') return 1; // eslint-disable-line
          else if (instructor2 === 'Staff') return -1;

          /** Use mapping to determine sort order */
          if (!res[name][instructor1]) return 1;
          else if (!res[name][instructor2]) return -1; // eslint-disable-line
          else {
            const sum1 = res[name][instructor1].reduce((a, b) => a + b, 0);
            const sum2 = res[name][instructor2].reduce((a, b) => a + b, 0);

            return (sum2 / res[name][instructor2].length) - (sum1 / res[name][instructor1].length);
          }
        }));
      })
      .finally(() => {
        const schedules = genSchedules(filteredCourseList, values.gap, values.sortByGPA, getState().grades);

        shortCode
          .then(code => dispatch(redirectToGenerator(schedules, code ? `q=${code}` : '')))
          .catch(() => dispatch(redirectToGenerator(schedules)))
          .then(() => dispatch(endGenerating(schedules)));
      });
  }
};

export default generateSchedules;
