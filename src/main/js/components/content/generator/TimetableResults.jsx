import React from 'react';
import CourseTable from '../../CourseTable';

const TimetableResults = ({ filteredCourses }) => {
  let rows = [];
  let noResults = false;
  filteredCourses.forEach((courseList, courseNum) => {
    const { filtered, removed, name } = courseList;
    rows.push((
      <tr key={rows.length}>
        <th colSpan="9">
          <h4>
            { `${name} - Restrictions Met (${filtered.length})` }
          </h4>
        </th>
      </tr>
    ));
    if (filtered.length > 0) {
      rows.push(<CourseTable.HeaderRow key={rows.length} />);
      filtered.forEach((course) => {
        const filteredRows = CourseTable.courseRows(course, courseNum);
        rows = rows.concat(filteredRows);
      });
    } else {
      noResults = true;
    }
    rows.push((
      <tr key={rows.length}>
        <th colSpan="9">
          <h4>
            { `${name} - Restrictions Unmet (${removed.length})` }
          </h4>
        </th>
      </tr>
    ));
    if (removed.length > 0) {
      rows.push(<CourseTable.HeaderRow key={rows.length} />);
      removed.forEach((course) => {
        const removedRows = CourseTable.courseRows(course, courseNum);
        rows = rows.concat(removedRows);
      });
    }
  });
  return (
    <div>
      <div className="page-header no-margin-bottom">
        <h1 className="o">
          Timetable Result
        </h1>
      </div>
      { noResults && (
        <div className="alert alert-warning">
          There are no schedules matching the restrictions!
        </div>
      )}
      <CourseTable>
        { rows }
      </CourseTable>
    </div>
  );
};

export default TimetableResults;
