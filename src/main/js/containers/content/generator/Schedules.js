import { connect } from 'react-redux';
import Schedules from '../../../components/content/generator/Schedules';
import { startGenerating, generateMore } from '../../../actions/generator';

const mapStateToProps = state => ({
  doneGenerating: state.generator.scheduleMaker
    ? state.generator.scheduleMaker.complete
    : true,
  schedules: state.generator.schedules,
  sortByGPA: state.generator.sort,
  gradeMap: state.grades.map,
});

const mapDispatchToProps = dispatch => ({
  generateMoreSchedules: () => {
    dispatch(startGenerating());
    dispatch(generateMore());
  },
});

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(Schedules);
