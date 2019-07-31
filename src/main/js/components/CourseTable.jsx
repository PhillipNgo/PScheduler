import React from 'react';
import colors from '../constants/colors';
import Schedule from '../utils/Schedule';

/**
* As of Fall 2019, VT puts the instructor for timetables in the format of
* 'X Y' where X is the first initial and Y is the instructor's last name
* @param {String} instructor is the name of the instructor
* @returns a String of the instructor's last name
*/
export const getInstructorLastName = (instructor) => {
  const splitted = instructor.split(' ');
  return splitted[splitted.length - 1];
};

export const calculateGPA = (schedule, gradeMap) => {
  if (!(schedule instanceof Schedule)) return 0.00;

  let weightedGPA = 0.00;
  let totalCredits = 0;

  schedule.forEach((course) => {
    const name = `${course.subject}${course.courseNumber}`;
    const instructor = getInstructorLastName(course.instructor);

    if (!gradeMap[name]) return;
    if (!gradeMap[name][instructor]) return;

    const sum = gradeMap[name][instructor].reduce((a, b) => a + b, 0);
    weightedGPA += (sum / gradeMap[name][instructor].length) * course.credits;
    totalCredits += course.credits;
  });

  const scheduleGPA = (weightedGPA === 0 || totalCredits === 0)
    ? 0.00 : weightedGPA / totalCredits;
  return scheduleGPA.toFixed(2);
};

/** Gets the gpa of a course */
export const getCourseGPA = (course, gradeMap) => {
  if (course.type === 'B') return 0.00;

  const name = `${course.subject}${course.courseNumber}`;
  const instructor = getInstructorLastName(course.instructor);

  if (!gradeMap[name]) return 0.00;
  if (!gradeMap[name][instructor]) return 0.00;
  const sum = gradeMap[name][instructor].reduce((a, b) => a + b, 0);

  return (sum / gradeMap[name][instructor].length).toFixed(2);
};

class CourseTable extends React.Component {

  /* eslint-disable react/no-array-index-key */
  createRows(sort) {
    const { courses, colored, gradeMap } = this.props;
    let rows = [];
    [...courses].forEach((course, courseNum) => {
      const courseRows = CourseTable.courseRows(course, courseNum, colored ? colors[courseNum] : '', sort, gradeMap);
      rows = rows.concat(courseRows);
    });
    return rows;
  }

  render() {
    const { courses, header, children = this.createRows(courses.sort), gradeMap } = this.props;
    return (
      <div className="table-responsive">
        <table className="table table-condensed text-table">
          <tbody>
            {header && <CourseTable.HeaderRow schedule={courses instanceof Schedule ? courses : null} gpa={calculateGPA(courses, gradeMap)} />}
            {children}
          </tbody>
        </table>
      </div>
    );
  }
}

CourseTable.HeaderRow = ({ schedule, gpa }) => (
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
    {schedule.sort === 'yes' && (
      <th>
        {`GPA${schedule ? ` (${gpa})` : ''}`}
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

CourseTable.courseRows = (course, index, color = '', sort, gradeMap) => (
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
      {sort === 'yes' && (
        <th>
          {`${getCourseGPA(course, gradeMap)}`}
        </th>
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
