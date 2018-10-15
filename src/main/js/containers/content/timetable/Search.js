import { connect } from 'react-redux';
import { reduxForm } from 'redux-form';
import Search from '../../../components/content/timetable/Search';
import { getCourseMapWithDispatch } from '../../../utils/search';
import { requestTimetableSearch, receiveTimetableSearch } from '../../../actions/timetable';

const mapStateToProps = state => ({
  isFetching: state.timetable.isFetching,
  courses: state.timetable.searchList,
});

const mapDispatchToProps = dispatch => ({
  fetchCourses: query => dispatch((thunkDispatch, getState) => {
    const { term } = getState().form.timetable_form.values;
    thunkDispatch(getCourseMapWithDispatch(
      { query, term },
      requestTimetableSearch,
      receiveTimetableSearch,
    ));
  }),
});

const form = connect(
  mapStateToProps,
  mapDispatchToProps,
)(Search);

export default reduxForm({
  form: 'timetable_form',
})(form);
