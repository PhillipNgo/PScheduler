import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import { parse } from 'query-string';
import { addToSchedule } from '../../actions/courses';
import formDefaults from '../../constants/formDefaults';
import generateSchedules from '../../utils/generate';
import {
  endGenerating,
  endRedirect,
  startFirstRender,
} from '../../actions/generator';
import Generator from '../../components/content/Generator';
import { getCourseMap } from '../../utils/search';

const mapStateToProps = state => ({
  isGenerating: state.generator.isGenerating,
  firstRender: !state.generator.initialValues,
  redirect: state.generator.redirect,
});

const mapDispatchToProps = dispatch => ({
  loadSchedule: (query) => {
    let formValues = {
      term: formDefaults.termValue,
      h1: '08',
      m1: '00',
      start: 'am',
      h2: '08',
      m2: '00',
      end: 'pm',
      gap: '15',
      free: [],
    };
    if (query) {
      try {
        const data = parse(query, { arrayFormat: 'bracket' });
        const courses = data.c ? data.c.map(course => JSON.parse(course)) : [];
        delete data.c;
        data.courses = {};
        Promise.all(courses.map(course => getCourseMap({ query: course.name, term: data.term })))
          .then((courseMaps) => {
            courseMaps.forEach((courseMap, index) => {
              if (!(courseMap instanceof Error)) {
                dispatch(addToSchedule(courseMap[courses[index].name], index));
                data.courses[`${courses[index].name}${index}`] = courses[index];
                delete courses[index].name;
              }
            });
          })
          .then(() => {
            formValues = { ...formValues, ...data };
            dispatch(generateSchedules(formValues));
            dispatch(startFirstRender(formValues));
          });
      } catch (e) {
        dispatch(startFirstRender(formValues));
        dispatch(endGenerating());
      }
    } else {
      dispatch(startFirstRender(formValues));
    }
  },
  redirected: () => dispatch(endRedirect()),
});

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(Generator));
