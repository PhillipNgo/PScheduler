/* eslint-disable no-underscore-dangle */
import Schedule from './Schedule';

class ScheduleMaker {
  constructor(courseListings, gap, sort) {
    this.courseListings = courseListings;
    this.gap = gap;
    this.sort = sort;
    this.schedules = [];
  }

  makeSchedules() {
    try {
      this._makeSchedules(this.courseListings, new Schedule(this.gap, this.sort), 0);
    } catch (e) {
      if (this.schedules.length !== ScheduleMaker.MAX_SCHEDULES) {
        return [];
      }
    }
    return this.schedules;
  }

  _makeSchedules(courseListings, schedule, courseIndex) {
    courseListings[courseIndex].forEach((course) => {
      const lastCourse = courseIndex === courseListings.length - 1;
      const currSchedule = lastCourse ? new Schedule(schedule, this.sort) : schedule;
      if (currSchedule.add(course)) {
        if (lastCourse) {
          this.schedules.push(currSchedule);
          // Reached MAX_SCHEDULES, forcibly quit recursion
          if (this.schedules.length === ScheduleMaker.MAX_SCHEDULES) {
            throw new Error();
          }
        } else {
          this._makeSchedules(courseListings, currSchedule, courseIndex + 1);
          schedule.delete(course);
        }
      }
    });
  }
}

ScheduleMaker.MAX_SCHEDULES = 100;

export default ScheduleMaker;
