import queryString from 'query-string';
import courseSearchUrl from '../constants/resources';
import 'whatwg-fetch';

const getCourseSearch = (values, requestAction, receiveAction) => (dispatch) => {
  dispatch(requestAction());
  const resource = `${courseSearchUrl}?${queryString.stringify({ ...values, size: 1000 })}`;
  fetch(resource)
    .then(response => response.json())
    .then((json) => {
      const courses = {};
      const courseList = process.env.NODE_ENV === 'production'
        ? json
        : json._embedded.courses; // eslint-disable-line no-underscore-dangle
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
      dispatch(receiveAction(courses));
    })
    .catch(error => dispatch(receiveAction(error)));
};

export default getCourseSearch;
