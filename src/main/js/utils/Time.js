/* eslint-disable no-underscore-dangle */
import moment from 'moment';

class TimeSlot {
  /**
   * Constructor
   * @param {String or Int} start a time in TimeSlot.timeFormat or number of minutes
   * @param {String or Int} end a time in TimeSlot.timeFormat or number of minutes
   */
  constructor(start, end) {
    let startTime;
    let endTime;
    if (Number.isInteger(start)) {
      startTime = moment().hours(0).minutes(start);
      this.start = startTime.format(TimeSlot.timeFormat);
      this.startNum = start;
    } else {
      startTime = moment(start, TimeSlot.timeFormat);
      this.start = startTime.format(TimeSlot.timeFormat);
      this.startNum = TimeSlot.timeNumber(moment(start, TimeSlot.timeFormat));
    }

    if (Number.isInteger(end)) {
      endTime = moment().hours(0).minutes(end);
      this.end = endTime.format(TimeSlot.timeFormat);
      this.endNum = end;
    } else {
      endTime = moment(end, TimeSlot.timeFormat);
      this.end = endTime.format(TimeSlot.timeFormat);
      this.endNum = TimeSlot.timeNumber(moment(end, TimeSlot.timeFormat));
    }
    this.isValid = startTime._isValid && endTime._isValid;
  }

  /**
   * Converts a moment into a minute number
   * @param {moment} time a specified time
   */
  static timeNumber(time) {
    const hour = time.hours();
    const minute = time.minutes();
    return (hour * 60) + minute;
  }

  static isValidString(time) {
    return moment(time, TimeSlot.timeFormat)._isValid;
  }

  /**
   * Checks if another TimeSlot conflicts with this TimeSlot
   * @param {TimeSlot} timeslot the TimeSlot to compare to
   * @param {int} gap minimum gap time between the TimeSlots
   */
  conflictsWith(timeSlot, gap) {
    if (!this.isValid || !timeSlot.isValid) {
      return false;
    }
    const gapStartTime = this.startNum - gap + 1;
    const gapEndTime = this.endNum + gap - 1;
    const { startNum, endNum } = timeSlot;

    if ((startNum >= gapStartTime && startNum <= gapEndTime)
      || (endNum >= gapStartTime && endNum <= gapEndTime)
      || (startNum >= gapStartTime && endNum <= gapEndTime)
      || (startNum <= gapStartTime && endNum >= gapEndTime)) {
      return true;
    }
    return false;
  }

  /**
   * Checks if another TimeSlot is within this time slot
   * @param {TimeSlot} timeslot the TimeSlot to compare to
   */
  contains(timeSlot) {
    if (!this.isValid || !timeSlot.isValid) {
      return true;
    }
    const { startNum, endNum } = timeSlot;
    return startNum >= this.startNum && endNum <= this.endNum;
  }
}

TimeSlot.timeFormat = 'h:mmA';

export default TimeSlot;
