import { connect } from 'react-redux';
import { reduxForm, resetSection } from 'redux-form';
import { resetSchedule, removeCourse } from '../../../actions/courses';
import generateSchedules from '../../../utils/generate';
import SearchForm from '../../../components/content/generator/SearchForm';

const mapStateToProps = state => ({
  schedule: state.courses.courseList,
  isFetching: state.courses.isFetching,
  firstRender: !state.form.generator_form,
  redirect: state.generator.redirect,
  formValues: state.generator.initialValues,
});

const mapDispatchToProps = dispatch => ({
  resetForm: () => dispatch(resetSchedule()),
  removeCourse: (course) => {
    dispatch(removeCourse(course));
    const section = `courses.${course[0].subject}${course[0].courseNumber}${course.id}`;
    dispatch(resetSection('generator_form', section));
  },
  submit: values => dispatch(generateSchedules(values)),
});

const form = connect(
  mapStateToProps,
  mapDispatchToProps,
)(SearchForm);

export default reduxForm({
  form: 'generator_form',
  destroyOnUnmount: false,
})(form);
