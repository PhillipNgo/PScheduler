import { connect } from 'react-redux';
import CourseTable from '../../../components/CourseTable';

const mapStateToProps = state => ({
  gradeMap: state.grades.map,
  sortByGPA: state.generator.sort,
});

const mapDispatchToProps = () => ({
});

export default connect(mapStateToProps, mapDispatchToProps)(CourseTable);
