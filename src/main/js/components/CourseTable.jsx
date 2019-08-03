import React from 'react';
import colors from '../constants/colors';
import Schedule from '../utils/Schedule';
import { getInstructorLastName } from '../utils/grade';

class CourseTable extends React.Component {
  getCourseGPA(course) {
    const { gradeMap } = this.props;

    const name = `${course.subject}${course.courseNumber}`;
    const instructor = getInstructorLastName(course.instructor);

    if (!gradeMap[name] || !gradeMap[name][instructor]) return 0.00;
    return gradeMap[name][instructor].toFixed(2);
  }

  /* eslint-disable react/no-array-index-key */
  createRows() {
    const { courses, colored, sortByGPA } = this.props;
    let rows = [];
    [...courses].forEach((course, courseNum) => {
      const courseRows = CourseTable.courseRows(course, courseNum, colored ? colors[courseNum] : '', sortByGPA, this.getCourseGPA(course));
      rows = rows.concat(courseRows);
    });
    return rows;
  }

  render() {
    const { sortByGPA, courses, header, children = this.createRows(), gradeMap } = this.props;
    return (
      <div className="table-responsive">
        <table className="table table-condensed text-table">
          <tbody>
            {header && <CourseTable.HeaderRow schedule={courses instanceof Schedule ? courses : null} gradeMap={gradeMap} sortByGPA={sortByGPA} />}
            {children}
          </tbody>
        </table>
      </div>
    );
  }
}

CourseTable.HeaderRow = ({ schedule, gradeMap, sortByGPA }) => (
  <tr>
    <th>
      CRN
    </th>
    <th>
      Course
    </th>
    <th>
      Title
    </th>
    {sortByGPA && (
      <th>
        {`GPA${schedule ? ` (${schedule.calculateGPA(gradeMap)})` : ''}`}
      </th>
    )}
    <th>
      Type
    </th>
    <th>
      {`Credits${schedule ? ` (${schedule.credits})` : ''}`}
    </th>
    <th>
      Instructor
    </th>
    <th>
      Days
    </th>
    <th>
      Time
    </th>
    <th>
      Location
    </th>
  </tr>
);

CourseTable.courseRows = (course, index, color = '', sort, grade) => (
  course.meetings.map((meeting, meetingNum) => (
    <tr style={{ backgroundColor: color }} key={`${course.crn}${meetingNum}${index}`}>
      <td>
        {meetingNum === 0 && course.crn}
      </td>
      <td>
        {meetingNum === 0 && `${course.subject} ${course.courseNumber}`}
      </td>
      <td>
        {meetingNum === 0 && course.name}
      </td>
      {sort && (
        <td>
          {grade}
        </td>
      )}
      <td>
        {meetingNum === 0 && course.type}
      </td>
      <td>
        {meetingNum === 0 && course.credits}
      </td>
      <td>
        {meetingNum === 0 && course.instructor}
      </td>
      <td>
        {meeting.days instanceof Array ? meeting.days.join(' ') : meeting.days}
      </td>
      <td>
        {`${meeting.startTime} - ${meeting.endTime}`}
      </td>
      <td>
        {meeting.location}
      </td>
    </tr>
  ))
);

export default CourseTable;
