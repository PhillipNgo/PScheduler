import queryString from 'query-string';
import { gpaSearchUrl } from '../constants/resources';

/**
 * Returns the estimated gpa of a course
 * @param course is the course to get the GPA of
 * @param grades is the array containing courses with grade distribution data
 */
function getGPA(course, grades) {
  let totalGPA = 0;
  let number = 0;

  const instructorFullName = course.instructor.split(' ');
  const instructorLastName = instructorFullName[instructorFullName.length - 1];

  grades.forEach((grade) => {
    if (instructorLastName === 'Staff' && grade.subject === course.subject && grade.courseNumber === course.courseNumber) {
      totalGPA += grade.gpa;
      number += 1;
    } else if (grade.subject === course.subject
        && grade.courseNumber === course.courseNumber
        && grade.instructor === instructorLastName) {
      totalGPA += grade.gpa;
      number += 1;
    }
  });

  const gpa = totalGPA / number;
  return !Number.isNaN(gpa) ? gpa.toFixed(2) : 0.00;
}

/**
 * Returns a string that represents the query value when calling the GPA API
 * @param courseList is a 2d array containing the list of courses
 */
function getQuerySearch(courseList) {
  const courseArr = [];
  courseList.forEach(subArray => courseArr.push(`${subArray[0].subject}${subArray[0].courseNumber}`));

  return queryString.stringify({ query: courseArr });
}

/**
 * Adds a gpa field to each course inside the courselist
 * @param courseList is a 2d array containing the list of courses
 */
function addGPA(courseList) {
  const query = getQuerySearch(courseList);
  const site = `${gpaSearchUrl}?${query}`;

  return fetch(site)
    .then(res => res.json()).then(res => res._embedded).then(res => res.gpa) // eslint-disable-line
    .then((res) => {
      const finalList = [];
      courseList.forEach((subArray, currentIndex) => {
        const gpaArray = subArray.map((course) => { // eslint-disable-line no-param-reassign
          const obj = Object.assign({}, course);
          obj.gpa = getGPA(course, res);
          return obj;
        });
        gpaArray.id = currentIndex;
        gpaArray.sort((course1, course2) => course2.gpa - course1.gpa);
        finalList.push(gpaArray);
      });
      return finalList;
    })
    .catch((error) => {
      throw new Error('addGPA to Schedule error: ', error);
    });
}

export default addGPA;
