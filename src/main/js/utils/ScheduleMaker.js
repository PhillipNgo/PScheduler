/* eslint-disable no-underscore-dangle */
import Schedule from './Schedule';

class ScheduleMaker {
  constructor(courseListings, gap) {
    this.courseListings = courseListings;
    this.gap = gap;
    this.schedules = [];
    this.currSchedule = new Schedule(gap);
    this.courseIndex = 0;
    this.addIndex = 0;
  }

  makeSchedules() {
    try {
      this._makeSchedules(this.courseListings, this.currSchedule);
    } catch (e) {
      console.log(this);
      if (this.schedules.length % ScheduleMaker.MAX_SCHEDULES !== 0) {
        return [];
      }
    }
    return this.schedules;
  }

  _makeSchedules(courseListings, schedule) {
    courseListings[this.courseIndex].slice(this.addIndex).forEach((course, index) => {
      const lastCourse = this.courseIndex === courseListings.length - 1;
      const currSchedule = lastCourse ? new Schedule(schedule) : schedule;
      if (currSchedule.add(course)) {
        if (lastCourse) {
          this.schedules.push(currSchedule);
          // Reached MAX_SCHEDULES, forcibly quit recursion
          if (this.schedules.length % ScheduleMaker.MAX_SCHEDULES === 0) {
            this.currSchedule = schedule;
            this.addIndex = index + 1;
            throw new Error();
          }
        } else {
          this.addIndex = 0;
          this.courseIndex += 1;
          this._makeSchedules(courseListings, currSchedule);
          this.courseIndex -= 1;
          currSchedule.delete(course);
        }
      }
    });
  }
}

ScheduleMaker.MAX_SCHEDULES = 2;

export default ScheduleMaker;
