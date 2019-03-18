import queryString from 'query-string';
import { courseSearchUrl } from '../constants/resources';
import 'whatwg-fetch';

export const getCourseList = values => (
  fetch(`${courseSearchUrl}?${
    queryString.stringify({
      ...values,
      query: process.env.NODE_ENV === 'production'
        ? JSON.stringify([...new Set(values.query)]) : [...new Set(values.query)],
      size: 1000,
    })}`)
    .then(response => response.json())
    .then(json => (process.env.NODE_ENV === 'production' ? json : json._embedded.courses)) // eslint-disable-line no-underscore-dangle
    .catch(error => error)
);

export const getCourseMap = values => (
  getCourseList(values)
    .then((courseList) => {
      const courses = {};
      courseList.some((course) => {
        const courseId = course.subject + course.courseNumber;
        if (!(courseId in courses)) {
          if (Object.keys(courses).length === 6) {
            return true;
          }
          courses[courseId] = [];
        }
        courses[courseId].push(course);
        return false;
      });
      return courses;
    })
);

export const getCourseMapWithDispatch = (values, requestAction, receiveAction) => (dispatch) => {
  dispatch(requestAction());
  getCourseMap(values)
    .then(courseMap => dispatch(receiveAction(courseMap)))
    .catch(error => dispatch(receiveAction(error)));
};

export const getCourseListWithDispatch = (values, requestAction, receiveAction) => (dispatch) => {
  dispatch(requestAction());
  getCourseList(values)
    .then(courseList => dispatch(receiveAction(courseList)))
    .catch(error => dispatch(receiveAction(error)));
};
