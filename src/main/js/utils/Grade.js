import queryString from 'query-string';
import 'whatwg-fetch';
import { gpaSearchUrl } from '../constants/resources';

const getMapping = (gradeList) => {
  const map = {};
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

  return map;
};

/**
 * Adds a gpa field to each course inside the courselist
 * @param courseList is a 2d array containing the list of courses
 */
const addGPA = (courseList) => {
  const set = new Set(courseList.map(list => `${list[0].subject}${list[0].courseNumber}`)); // removes duplicates
  const query = queryString.stringify({ query: [...set] });
  const site = `${gpaSearchUrl}?${query}`;

  return fetch(site)
    .then(res => res.json()).then(res => res._embedded).then(res => res.gpa) // eslint-disable-line
    .then((res) => {
      const mapping = getMapping(res);
      return mapping;
    })
    .catch((error) => {
      throw new Error('addGPA to Schedule error: ', error);
    });
};

export default addGPA;
