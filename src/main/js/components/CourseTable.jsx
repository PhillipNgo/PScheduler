import React from 'react';
import colors from '../constants/colors';
import Schedule from '../utils/Schedule';
import { getInstructorLastName } from '../utils/grade';

class CourseTable extends React.Component {
  constructor(props) {
    super(props);
    this.HeaderRow = this.HeaderRow.bind(this);
  }

  getCourseGPA(course) {
    const { gradeMap, useCourseAvg } = this.props;

    const name = `${course.subject}${course.courseNumber}`;
    const instructor = getInstructorLastName(course.instructor);

    if (!gradeMap[name]) {
      return 'DNE';
    }

    if (!gradeMap[name][instructor]) {
      return useCourseAvg ? `${gradeMap[name].AVERAGE.toFixed(2)}*` : 'DNE';
    }

    return `${gradeMap[name][instructor].toFixed(2)}`;
  }

  HeaderRow({ schedule }) {
    const { sortByGPA, gradeMap, useCourseAvg } = this.props;

    return (
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
            {`GPA${schedule ? ` (${schedule.calculateGPA(gradeMap, useCourseAvg)})` : ''}`}
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
  }

  /* eslint-disable react/no-array-index-key */
  createRows() {
    const { courses, sortByGPA, colored } = this.props;
    const rows = [];

    [...courses].forEach((course, courseNum) => {
      course.meetings.forEach((meeting, meetingNum) => rows.push((
        <tr
          style={{ backgroundColor: colored ? colors[courseNum] : '' }}
          key={`${course.crn}${meetingNum}${courseNum}`}
        >
          <td>
            {meetingNum === 0 && course.crn}
          </td>
          <td>
            {meetingNum === 0 && `${course.subject} ${course.courseNumber}`}
          </td>
          <td>
            {meetingNum === 0 && course.name}
          </td>
          {sortByGPA && (
            <td>
              {meetingNum === 0 && this.getCourseGPA(course)}
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
      )));
    });

    return rows;
  }

  render() {
    const { courses, header, children = this.createRows() } = this.props;

    return (
      <div className="table-responsive">
        <table className="table table-condensed text-table">
          <tbody>
            {header && (<this.HeaderRow schedule={courses instanceof Schedule ? courses : null} />)}
            {children}
          </tbody>
        </table>
      </div>
    );
  }
}

export default CourseTable;
