import { connect } from 'react-redux';
import TimetableResults from '../../../components/content/generator/TimetableResults';

const mapStateToProps = state => ({
  filteredCourses: state.generator.filteredCourses,
});

const mapDispatchToProps = () => ({});

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(TimetableResults);
