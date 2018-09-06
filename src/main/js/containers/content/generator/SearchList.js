import { connect } from 'react-redux';
import { reduxForm, change } from 'redux-form';
import SearchList from '../../../components/SearchList';
import getCourseSearch from '../../../utils/search';
import {
  addToSchedule,
  requestCourseSearch,
  receiveCourseSearch,
} from '../../../actions/courses';

const mapStateToProps = state => ({
  courses: state.courses.searchList,
});

const idExists = (courseList, courseName, id) => courseList.find(list => `${list[0].subject}${list[0].courseNumber}` === courseName && list.id === id);

const mapDispatchToProps = dispatch => ({
  fetchCourses: query => dispatch((thunkDispatch, getState) => {
    const { term } = getState().form.generator_form.values;
    thunkDispatch(getCourseSearch({ query, term }, requestCourseSearch, receiveCourseSearch));
  }),
  addToSchedule: (courses, course) => dispatch((thunkDispatch, getState) => {
    let id = 0;
    const courseName = `${courses[0].subject}${courses[0].courseNumber}`;
    while (idExists(getState().courses.courseList, courseName, id)) {
      id += 1;
    }
    thunkDispatch(addToSchedule(courses, id));
    if (course) {
      thunkDispatch(change('generator_form', `courses.${courseName}${id}.crns`, [`${course.crn}`]));
    }
  }),
});

const form = connect(
  mapStateToProps,
  mapDispatchToProps,
)(SearchList);

export default reduxForm({
  form: 'search_form_generator',
})(form);
