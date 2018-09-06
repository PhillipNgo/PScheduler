import React from 'react';
import { FormSection } from 'redux-form';
import colors from '../../../constants/colors';
import Schedule from '../../../utils/Schedule';

class CourseTable extends React.Component {
  componentDidUpdate() {
    $("[data-toggle='popover']").popover();
  }

  switchWillConflict(selected, course) {
    const { schedule } = this.props;
    const newSchedule = new Schedule(schedule);
    newSchedule.delete(selected);
    return newSchedule.conflictsWith(course);
  }

  createCrnSelect(courses) {
    const {
      selectCourse,
      removeFromTable,
      unselectCourse,
    } = this.props;
    return (
      <div className="btn-group">
        <button type="button" className="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          { courses.selected ? courses.selected.crn : 'None' }
          {' '}
          <span className="caret" />
        </button>
        <ul className="dropdown-menu">
          <li
            onClick={() => {
              unselectCourse(courses.selected.subject, courses.selected.courseNumber, courses.id);
            }}
          >
            <a>
              None
            </a>
          </li>
          {
            courses.courses.map(course => (
              <li
                className={this.switchWillConflict(courses.selected, course) ? 'disabled' : ''}
                key={course.crn}
                onClick={() => selectCourse(`${course.subject}${course.courseNumber}`, courses.id, `${course.crn}`)}
              >
                <a
                  data-toggle="popover"
                  data-trigger="hover"
                  data-content={
                    `${course.instructor}\n${
                      course.type}\n${
                      course.meetings.map(meeting => (
                        `${meeting.days} / ${
                          meeting.startTime} - ${
                          meeting.endTime} / ${
                          meeting.location}`
                      )).join('\n')}`
                  }
                  value={`${course.crn}`}
                >
                  {`${course.crn}`}
                </a>
              </li>
            ))
          }
          <li role="separator" className="divider" />
          <li onClick={() => removeFromTable(courses)}>
            <a>
              Remove
            </a>
          </li>
        </ul>
      </div>
    );
  }

  /* eslint-disable react/no-array-index-key */
  render() {
    const { courses } = this.props;
    const rows = [];
    courses.forEach((course, courseNum) => {
      const { selected } = course;
      rows.push(...selected.meetings.map((meeting, meetingNum) => (
        <tr style={{ backgroundColor: colors[courseNum] }} key={`${course.id}-${selected.crn}-${courseNum}-${meetingNum}`}>
          <td>
            { meetingNum === 0 && this.createCrnSelect(course) }
          </td>
          <td>
            { meetingNum === 0 && `${selected.subject} ${selected.courseNumber}` }
          </td>
          <td>
            { meetingNum === 0 && selected.name }
          </td>
          <td>
            { meetingNum === 0 && selected.type }
          </td>
          <td>
            { meetingNum === 0 && selected.credits }
          </td>
          <td>
            { meetingNum === 0 && selected.instructor }
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
      )));
    });
    return (
      <FormSection name="crns">
        <div className="">
          <table className="table table-condensed text-table">
            <tbody>
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
                  Credits
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
              { rows }
            </tbody>
          </table>
        </div>
      </FormSection>
    );
  }
}

export default CourseTable;
