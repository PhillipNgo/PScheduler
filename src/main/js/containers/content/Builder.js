import { connect } from 'react-redux';
import { reduxForm } from 'redux-form';
import { parse } from 'query-string';
import Builder from '../../components/content/Builder';
import { getCourseMap, getCourseMapWithDispatch } from '../../utils/search';
import {
  requestBuilderSearch,
  receiveBuilderSearch,
  resetBuilder,
  addToBuilder,
} from '../../actions/builder';

const mapStateToProps = (state) => ({
  isFetching: state.builder.isFetching,
  courseList: state.builder.courseList,
  term: state.form.builder_form && state.form.builder_form.values.term,
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
    if (query) {
      const queryObject = parse(query, { arrayFormat: "bracket" });
      const data = {};
      queryObject.c.forEach((course) => {
        const split = course.split('+');
        data[split[0]] = split.length === 2 ? split[1] : null;
      });
      const q = Object.keys(data);
      getCourseMap({ query: process.env.NODE_ENV === 'production' ? JSON.stringify(q) : q, term: data.term })
        .then((courseMap) => {
          q.forEach((name, index) => {
            const courses = courseMap[name];
            if (courses) {
              dispatch(addToBuilder(courses, index, courses.find(course => course.crn === data[name])));
            }
          });
        })
        .then(() => {
          // dispatch(startFirstRender({}));
        })
        .catch((e) => {
          // dispatch(startFirstRender(formValues));
          // dispatch(endGenerating(e));
        });
    } else {
      // dispatch(startFirstRender(formValues));
    }
  },
});

const form = connect(
  mapStateToProps,
  mapDispatchToProps,
)(Builder);

export default reduxForm({
  form: 'builder_form',
  enableReinitialize: true,
})(form);
