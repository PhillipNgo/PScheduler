import React from 'react';
import TimeSlot from '../utils/Time';
import colors from '../constants/colors';

class ScheduleVisualTable extends React.Component {
  static earliestTime(scheduleTable) {
    let earliest = Math.min(...scheduleTable.map(daySchedule => (
      daySchedule.length ? daySchedule[0].timeSlot.startNum : Infinity
    )));
    earliest = earliest !== Infinity ? earliest : 0;
    return Math.floor(earliest / 60) * 60;
  }

  static latestTime(scheduleTable) {
    let latest = Math.max(...scheduleTable.map(daySchedule => (
      daySchedule.length ? daySchedule[daySchedule.length - 1].timeSlot.endNum : -Infinity
    )));
    latest = latest !== -Infinity ? latest : 60;
    return Math.ceil(latest / 60) * 60;
  }

  createScheduleTable() {
    const { schedule } = this.props;

    // Manages meeting data, represent the five days of the week
    const scheduleTable = [[], [], [], [], []];

    // Maps day names to their spot in the table for easy reference
    const scheduleTableMap = {
      M: scheduleTable[0], // Monday meetings
      T: scheduleTable[1], // Tuesday meetings
      W: scheduleTable[2], // Wednesday meetings
      R: scheduleTable[3], // Thursday meetings
      F: scheduleTable[4], // Friday meetings
    };

    [...schedule].forEach((course, courseNum) => {
      course.meetings.forEach((meeting) => {
        const meetingData = {
          subject: course.subject,
          courseNumber: course.courseNumber,
          timeSlot: meeting.timeSlot,
          location: meeting.location,
          instructor: course.instructor,
          color: colors[courseNum],
        };
        if (meeting.timeSlot.isValid) {
          meeting.days.forEach((day) => {
            const daySchedule = scheduleTableMap[day];
            const insertIndex = daySchedule.findIndex(dayMeeting => (
              meeting.timeSlot.startNum < dayMeeting.timeSlot.startNum
            ));
            if (insertIndex < 0) {
              daySchedule.push(meetingData);
            } else {
              daySchedule.splice(insertIndex, 0, meetingData);
            }
          });
        }
      });
    });

    // Create time slots, which will go before the meetings
    const lowest = ScheduleVisualTable.earliestTime(scheduleTable);
    const highest = ScheduleVisualTable.latestTime(scheduleTable);
    scheduleTable.unshift([]);
    for (let time = lowest; time < highest; time += 60) {
      scheduleTable[0].push({
        timeSlot: new TimeSlot(time, time + 60),
      });
    }

    return scheduleTable;
  }

  /* eslint-disable react/no-array-index-key, no-loop-func */
  createRows() {
    const scheduleTable = this.createScheduleTable();
    const lowest = ScheduleVisualTable.earliestTime(scheduleTable);
    const highest = ScheduleVisualTable.latestTime(scheduleTable);

    const rows = [];
    const scheduleIndex = [0, 0, 0, 0, 0, 0];
    const earliestTimes = [lowest, lowest, lowest, lowest, lowest, lowest];
    let earliestTime = earliestTimes.reduce((earliest, time) => (
      time < earliest ? time : earliest
    ));

    while (!earliestTimes.every(time => time === highest)) {
      const nextRow = [];
      scheduleTable.forEach((daySchedule, index) => {
        const key = `${rows.length}${index}${scheduleIndex[index]}`;
        if (scheduleIndex[index] < scheduleTable[index].length) {
          const meeting = daySchedule[scheduleIndex[index]];
          const { startNum, endNum, start } = meeting.timeSlot;
          if (startNum === earliestTime) {
            if (index === 0) {
              nextRow.push(<ScheduleVisualTable.TimeCell time={start} key={key} />);
            } else {
              nextRow.push(<ScheduleVisualTable.CourseCell meetingData={meeting} key={key} />);
            }
            earliestTimes[index] = endNum;
            scheduleIndex[index] += 1;
          } else if (earliestTime === earliestTimes[index]) {
            nextRow.push(<td rowSpan={(startNum - earliestTime) / 5} key={key} />);
            earliestTimes[index] = startNum;
          }
        } else if (earliestTime === earliestTimes[index]) {
          nextRow.push(<td rowSpan={(highest - earliestTimes[index]) / 5} key={key} />);
          earliestTimes[index] = highest;
        }
      });
      rows.push((
        <tr key={rows.length}>
          { nextRow }
        </tr>
      ));
      const nextEarliestTime = earliestTimes.reduce((earliest, time) => (
        time < earliest ? time : earliest
      ));
      const padRows = ((nextEarliestTime - earliestTime) / 5) - 1;
      for (let i = 0; i < padRows; i += 1) {
        rows.push(<tr key={rows.length} />);
      }
      earliestTime = nextEarliestTime;
    }
    return rows;
  }

  render() {
    return (
      <div className="table-responsive">
        <table className="visual-table">
          <tbody>
            <tr>
              <th className="time-column" />
              <th className="day-column">
                Monday
              </th>
              <th className="day-column">
                Tuesday
              </th>
              <th className="day-column">
                Wednesday
              </th>
              <th className="day-column">
                Thursday
              </th>
              <th className="day-column">
                Friday
              </th>
            </tr>
            { this.createRows() }
          </tbody>
        </table>
      </div>
    );
  }
}

ScheduleVisualTable.CourseCell = ({ meetingData }) => (
  <td
    className="bordered meeting-cell"
    rowSpan={(meetingData.timeSlot.endNum - meetingData.timeSlot.startNum) / 5}
    style={{ backgroundColor: meetingData.color }}
  >
    { `${meetingData.subject} ${meetingData.courseNumber}` }
    <br />
    { `${meetingData.timeSlot.start} - ${meetingData.timeSlot.end} ` }
    <br />
    { meetingData.location }
    <br />
    { meetingData.instructor }
  </td>
);

ScheduleVisualTable.TimeCell = ({ time }) => (
  <td className="bordered" rowSpan={12}>
    { time }
  </td>
);

export default ScheduleVisualTable;
