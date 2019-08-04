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

const getMapping = (gradeList, useCourseAvg) => {
  const map = {};
  gradeList.forEach((course) => {
    const name = `${course.subject}${course.courseNumber}`;

    if (!map[name]) {
      map[name] = {};
    }

    if (!map[name][course.instructor]) {
      map[name][course.instructor] = { qualityCredits: 0, credits: 0 };
    }

    map[name][course.instructor].qualityCredits += course.gpa * course.credits;
    map[name][course.instructor].credits += course.credits;
  });

  /** Transform array of grades into average */
  Object.keys(map).forEach((name) => {
    Object.keys(map[name]).forEach((instructor) => {
      map[name][instructor] = map[name][instructor].qualityCredits / map[name][instructor].credits;
    });
  });

  if (useCourseAvg) {
    Object.keys(map).forEach((name) => {
      let count = 0;
      let sum = 0;
      Object.keys(map[name]).forEach((instructor) => {
        sum += map[name][instructor];
        count += 1;
      });
      map[name].Staff = sum / count;
    });
  }
  return map;
};

/**
 * Get a mapping of course-instructor to average GPA
 * @param courseList is a 2d array containing the list of courses
 * @param useCourseAvg is a boolean denoting if we need to add a course average gpa
 */
const getGPAMap = (courseList, useCourseAvg) => {
  const set = new Set(courseList.map(list => `${list[0].subject}${list[0].courseNumber}`));
  const query = queryString.stringify({ query: [...set] });
  const site = `${gpaSearchUrl}?${query}`;

  return fetch(site)
    .then(res => res.json())
    .then(json => getMapping(json._embedded.gpa, // eslint-disable-line no-underscore-dangle
      useCourseAvg));
};

export default getGPAMap;
