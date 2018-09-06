import { connect } from 'react-redux';
import { reduxForm, resetSection } from 'redux-form';
import { resetSchedule, removeCourse } from '../../../actions/courses';
import { endRedirect } from '../../../actions/generator';
import generateSchedules from '../../../utils/generate';
import SearchForm from '../../../components/content/generator/SearchForm';

const mapStateToProps = state => ({
  schedule: state.courses.courseList,
  isFetching: state.courses.isFetching,
  firstRender: !state.form.generator_form,
  redirect: state.generator.redirect,
});

const mapDispatchToProps = dispatch => ({
  resetForm: () => dispatch(resetSchedule()),
  removeCourse: course => dispatch((thunkDispatch) => {
    thunkDispatch(removeCourse(course));
    const section = `courses.${course[0].subject}${course[0].courseNumber}${course.id}`;
    thunkDispatch(resetSection('generator_form', section));
  }),
  submit: values => dispatch(generateSchedules(values)),
  redirected: () => dispatch(endRedirect()),
});

const form = connect(
  mapStateToProps,
  mapDispatchToProps,
)(SearchForm);

export default reduxForm({
  form: 'generator_form',
  destroyOnUnmount: false,
})(form);
