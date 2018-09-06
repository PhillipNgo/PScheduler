import TimeSlot from './Time';

const filterCrns = (crns = [], course) => (
  crns.length === 0 || crns.find(crn => `${course.crn}` === crn)
);

const filterTypes = (types = [], course) => (
  types.length === 0 || types.find(type => course.type.toLowerCase() === type.toLowerCase())
);

const filterInstructors = (instructors = [], course) => (
  instructors.length === 0 || instructors.find(instructor => (
    course.instructor.toLowerCase() === instructor.toLowerCase()
  ))
);

const filterFreeDays = (freeDays = [], course) => (
  freeDays.length === 0
    || !course.meetings.some(meeting => meeting.days)
      .some(day => freeDays.find(free => day.toLowerCase() === free.toLowerCase()))
);

const filterTime = (start, end, course) => {
  const filter = new TimeSlot(start, end);
  return !course.meetings || course.meetings.every((meeting) => {
    const { startTime, endTime } = meeting;
    const meetingTime = new TimeSlot(startTime, endTime);
    return filter.contains(meetingTime);
  });
};

const filterCourses = (values, courseList) => {
  const filteredCourseList = [];
  courseList.forEach((list) => {
    const currList = {
      name: `${list[0].subject} ${list[0].courseNumber}`,
    };
    filteredCourseList.push(currList);
    currList.removed = [];
    let filtered = list.filter((course) => {
      const keep = filterFreeDays(values.free, course)
        && filterTime(`${values.h1}:${values.m1}${values.start}`, `${values.h2}:${values.m2}${values.end}`, course);
      if (!keep) {
        currList.removed.push(course);
      }
      return keep;
    });
    const key = `${list[0].subject}${list[0].courseNumber}${list.id}`;
    if (values.courses && values.courses[key]) {
      const filters = values.courses[key];
      filtered = filtered.filter((course) => {
        const keep = filterCrns(filters.crns, course)
          && filterTypes(filters.types, course)
          && filterInstructors(filters.instructors, course);
        if (!keep) {
          currList.removed.push(course);
        }
        return keep;
      });
    }
    currList.filtered = filtered;
  });
  return filteredCourseList;
};

export default filterCourses;
