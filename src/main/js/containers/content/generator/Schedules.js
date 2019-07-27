import { connect } from 'react-redux';
import { generateMoreSchedules } from '../../../utils/generate';
import Schedules from '../../../components/content/generator/Schedules';

const mapStateToProps = state => ({
  schedules: state.generator.error ? [] : state.generator.scheduleMaker.schedules,
});

const mapDispatchToProps = dispatch => ({
  loadMoreSchedules: () => dispatch(generateMoreSchedules()),
});

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(Schedules);
