import { connect } from 'react-redux';
import CourseTable from '../../../components/CourseTable';

const mapStateToProps = (state, ownProps) => ({
  ...ownProps,
  gradeMap: state.grades.map,
  sortByGPA: state.generator.sort,
});

export default connect(mapStateToProps, null)(CourseTable);
