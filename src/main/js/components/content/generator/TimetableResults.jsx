import React from 'react';
import CourseTable from '../../CourseTable';

const TimetableResults = ({ filteredCourses, schedules }) => (
  <div>
    <div className="page-header">
      <h1 className="o">
        Timetable Result
      </h1>
    </div>
    { schedules.length === 0 && (
      <div className="alert alert-warning">
        There are no schedules matching the restrictions!
        This can be caused by courses that don&#39;t meet your restrictions
        or courses that meet the restrictions but are always conflicting.
      </div>
    )}
    <h3>
      Met Restrictions
    </h3>
    <CourseTable
      courses={
        filteredCourses.map(courseList => courseList.filtered).flat()
      }
      header
    />
    <h3>
      Unmet Restrictions
    </h3>
    <CourseTable
      courses={
        filteredCourses.map(courseList => courseList.removed).flat()
      }
      header
    />
  </div>
);

export default TimetableResults;
