import { connect } from 'react-redux';
import { reduxForm } from 'redux-form';
import { parse } from 'query-string';
import Builder from '../../components/content/Builder';
import formDefaults from '../../constants/formDefaults';
import { getCourseMap, getCourseMapWithDispatch } from '../../utils/search';
import {
  requestBuilderSearch,
  receiveBuilderSearch,
  resetBuilder,
  addToBuilder,
  startBuilding,
  endBuilding,
} from '../../actions/builder';

const mapStateToProps = state => ({
  isFetching: state.builder.isFetching,
  isBuilding: state.builder.isBuilding,
  courseList: state.builder.courseList,
  term: state.form.builder_form && state.form.builder_form.values
    && state.form.builder_form.values.term,
  firstRender: !state.builder.initialValues,
});

const mapDispatchToProps = dispatch => ({
  fetchCourses: query => dispatch((thunkDispatch, getState) => {
    const { term } = getState().form.builder_form.values;
    thunkDispatch(getCourseMapWithDispatch(
      { query, term },
      requestBuilderSearch,
      receiveBuilderSearch,
    ));
  }),
  resetBuilder: () => dispatch(resetBuilder()),
  loadSchedule: (query) => {
    const formValues = {
      term: formDefaults.termValue,
      ...parse(query, { arrayFormat: 'bracket' }),
    };

    dispatch(startBuilding(formValues));

    if (query && formValues.c) {
      const data = [];

      formValues.c.forEach((course) => {
        const split = course.split(' ');
        data.push({ name: split[0], crn: split.length === 2 ? split[1] : null });
      });
      const q = data.map(val => val.name);

      getCourseMap({ query: q, term: formValues.term })
        .then((courseMap) => {
          data.forEach((val, index) => {
            const courses = courseMap[val.name];
            if (courses) {
              dispatch(addToBuilder(courses, index,
                courses.find(course => `${course.crn}` === val.crn)));
            }
          });
        })
        .then(() => {
          dispatch(endBuilding());
        })
        .catch(() => {
          dispatch(endBuilding());
        });
    } else {
      dispatch(endBuilding());
    }
  },
});

const form = connect(
  mapStateToProps,
  mapDispatchToProps,
)(Builder);

export default reduxForm({
  form: 'builder_form',
  destroyOnUnmount: false,
  enableReinitialize: true,
})(form);
