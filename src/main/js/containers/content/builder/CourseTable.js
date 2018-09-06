import { connect } from 'react-redux';
import CourseTable from '../../../components/content/builder/CourseTable';
import { removeFromBuilder, selectCrn, unselectCrn } from '../../../actions/builder';

const mapStateToProps = state => ({
  courses: state.builder.courseList,
  schedule: state.builder.schedule,
});

const mapDispatchToProps = dispatch => ({
  selectCourse: (courseName, id, crn) => dispatch(selectCrn(courseName, id, crn)),
  removeFromTable: courseName => dispatch(removeFromBuilder(courseName)),
  unselectCourse: (subject, courseNumber, id) => dispatch(unselectCrn(subject, courseNumber, id)),
});

export default connect(
  mapStateToProps,
  mapDispatchToProps,
)(CourseTable);
