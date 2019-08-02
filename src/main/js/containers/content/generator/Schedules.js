import { connect } from 'react-redux';
import Schedules from '../../../components/content/generator/Schedules';

const mapStateToProps = state => ({
  schedules: state.generator.schedules,
  sortByGPA: state.generator.sort,
  gradeMap: state.grades.map,
});

const mapDispatchToProps = () => ({
});

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(Schedules);
