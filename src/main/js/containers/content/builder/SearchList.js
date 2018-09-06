import { connect } from 'react-redux';
import SearchList from '../../../components/SearchList';
import getCourseSearch from '../../../utils/search';
import {
  requestBuilderSearch,
  receiveBuilderSearch,
  addToBuilder,
  removeFromBuilder,
} from '../../../actions/builder';

const idExists = (courseList, courseName, id) => courseList.find(list => `${list.courses[0].subject}${list.courses[0].courseNumber}` === courseName && list.id === id);

const mapStateToProps = state => ({
  courses: state.builder.searchList,
  courseList: state.builder.courseList,
});

const mapDispatchToProps = dispatch => ({
  fetchCourses: query => dispatch((thunkDispatch, getState) => {
    const { term } = getState().form.builder_form.values;
    thunkDispatch(getCourseSearch({ query, term }, requestBuilderSearch, receiveBuilderSearch));
  }),
  addToSchedule: (courses, course) => dispatch((thunkDispatch, getState) => {
    let id = 0;
    const courseName = `${courses[0].subject}${courses[0].courseNumber}`;
    while (idExists(getState().builder.courseList, courseName, id)) {
      id += 1;
    }
    thunkDispatch(addToBuilder(courses, id, course));
  }),
  removeFromSchedule: courseName => dispatch(removeFromBuilder(courseName)),
});

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(SearchList);
