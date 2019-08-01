import queryString from 'query-string';
import 'whatwg-fetch';
import { gpaSearchUrl } from '../constants/resources';

/**
* As of Fall 2019, VT puts the instructor for timetables in the format of
* 'X Y' where X is the first initial and Y is the instructor's last name
* @param {String} instructor is the name of the instructor
* @returns a String of the instructor's last name
*/
export const getInstructorLastName = instructor => instructor.split(' ').pop();

const getMapping = (gradeList) => {
  const map = {};
  /** map array of grades to its respective course-instructor */
  gradeList.forEach((course) => {
    const name = `${course.subject}${course.courseNumber}`;

    if (!map[name]) {
      map[name] = {};
      map[name][course.instructor] = [course.gpa];
    } else if (!map[name][course.instructor]) {
      map[name][course.instructor] = [course.gpa];
    } else {
      map[name][course.instructor] = [...map[name][course.instructor], course.gpa];
    }
  });

  /** Transform array of grades into average */
  Object.keys(map).forEach((name) => {
    Object.keys(map[name]).forEach((instructor) => {
      const sum = map[name][instructor].reduce((a, b) => a + b, 0);
      map[name][instructor] = sum / map[name][instructor].length;
    });
  });
  return map;
};

/**
 * Adds a gpa field to each course inside the courselist
 * @param courseList is a 2d array containing the list of courses
 */
const getGPAMap = (courseList) => {
  const set = new Set(courseList.map(list => `${list[0].subject}${list[0].courseNumber}`)); // removes duplicates
  const query = queryString.stringify({ query: [...set] });
  const site = `${gpaSearchUrl}?${query}`;

  return fetch(site)
    .then(res => res.json())
    .then(json => getMapping(json._embedded.gpa)) // eslint-disable-line
    .catch(error => error);
};

export default getGPAMap;
