import React from 'react';
import colors from '../constants/colors';
import Schedule from '../utils/Schedule';

class CourseTable extends React.Component {
  /* eslint-disable react/no-array-index-key */
  createRows() {
    const { courses, colored } = this.props;
    let rows = [];
    [...courses].forEach((course, courseNum) => {
      const courseRows = CourseTable.courseRows(course, courseNum, colored ? colors[courseNum] : '');
      rows = rows.concat(courseRows);
    });
    return rows;
  }

  render() {
    const { courses, header, children = this.createRows() } = this.props;
    return (
      <div className="table-responsive">
        <table className="table table-condensed text-table">
          <tbody>
            { header && <CourseTable.HeaderRow schedule={courses instanceof Schedule ? courses : null} /> }
            { children }
          </tbody>
        </table>
      </div>
    );
  }
}

CourseTable.HeaderRow = ({ schedule }) => (
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

CourseTable.courseRows = (course, index, color = '') => (
  course.meetings.map((meeting, meetingNum) => (
    <tr style={{ backgroundColor: color }} key={`${course.crn}${meetingNum}${index}`}>
      <td>
        { meetingNum === 0 && course.crn }
      </td>
      <td>
        { meetingNum === 0 && `${course.subject} ${course.courseNumber}`}
      </td>
      <td>
        { meetingNum === 0 && course.name }
      </td>
      <td>
        { meetingNum === 0 && course.type }
      </td>
      <td>
        { meetingNum === 0 && course.credits }
      </td>
      <td>
        { meetingNum === 0 && course.instructor }
      </td>
      <td>
        { meeting.days instanceof Array ? meeting.days.join(' ') : meeting.days }
      </td>
      <td>
        { `${meeting.startTime} - ${meeting.endTime}` }
      </td>
      <td>
        { meeting.location }
      </td>
    </tr>
  ))
);

export default CourseTable;
