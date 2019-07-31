import { connect } from 'react-redux';
import Schedules from '../../../components/content/generator/Schedules';

const mapStateToProps = state => ({
  schedules: state.generator.schedules,
  gradeMap: state.grades,
});

const mapDispatchToProps = () => ({
});

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(Schedules);
