import { connect } from 'react-redux';
import { reduxForm } from 'redux-form';
import Builder from '../../components/content/Builder';
import getCourseSearch from '../../utils/search';
import { requestBuilderSearch, receiveBuilderSearch, resetBuilder } from '../../actions/builder';

const mapStateToProps = (state) => {
  const schedule = [...state.builder.schedule];
  return {
    isFetching: state.builder.isFetching,
    schedule: state.builder.courseList
      .map(list => schedule.find(course => course === list.selected)),
  };
};

const mapDispatchToProps = dispatch => ({
  fetchCourses: query => dispatch((thunkDispatch, getState) => {
    const { term } = getState().form.builder_form.values;
    thunkDispatch(getCourseSearch(
      { query, term },
      requestBuilderSearch,
      receiveBuilderSearch,
    ));
  }),
  resetBuilder: () => dispatch(resetBuilder()),
});

const form = connect(
  mapStateToProps,
  mapDispatchToProps,
)(Builder);

export default reduxForm({
  form: 'builder_form',
  enableReinitialize: true,
})(form);
