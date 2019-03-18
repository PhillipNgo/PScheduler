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
  startBuilding,
  endBuilding,
} from '../../actions/builder';

const mapStateToProps = state => ({
  isFetching: state.builder.isFetching,
  isBuilding: state.builder.isBuilding,
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
    dispatch(startBuilding());
    if (query) {
      const queryObject = parse(query, { arrayFormat: 'bracket' });
      try {
        const data = {};
        queryObject.c.forEach((course) => {
          const split = course.split(' ');
          data[split[0]] = split.length === 2 ? split[1] : null;
        });
        const q = Object.keys(data);
        getCourseMap({ query: process.env.NODE_ENV === 'production' ? JSON.stringify(q) : q, term: data.term })
          .then((courseMap) => {
            q.forEach((name, index) => {
              const courses = courseMap[name];
              if (courses) {
                dispatch(addToBuilder(courses, index,
                  courses.find(course => `${course.crn}` === data[name])));
              }
            });
          })
          .then(() => {
            dispatch(endBuilding());
          })
          .catch((e) => {
            dispatch(endBuilding());
            // dispatch(endGenerating(e));
          });
      } catch (err) {

      }
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
  enableReinitialize: true,
})(form);
