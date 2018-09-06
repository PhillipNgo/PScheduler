import React from 'react';
import FormModule from './FormModule';

class SearchList extends React.Component {
  constructor(props) {
    super(props);
    this.showSearch = this.showSearch.bind(this);
    this.hideSearch = this.hideSearch.bind(this);
    this.clickListener = this.clickListener.bind(this);
    this.state = {
      showSearch: false,
      timer: null,
      interval: 750,
    };
  }

  componentDidMount() {
    window.addEventListener('click', this.clickListener);
  }

  componentDidUpdate() {
    $("[data-toggle='popover']").popover();
  }

  componentWillUnmount() {
    window.removeEventListener('click', this.clickListener);
  }

  clickListener(e) {
    if (e.target.name !== 'class_search') {
      this.hideSearch();
    }
  }

  // Triggers a course search after an interval amount of time of no typing
  searchOnChange(query) {
    const { timer, interval } = this.state;
    const { fetchCourses } = this.props;
    clearTimeout(timer);
    if (query && query.length > 1) {
      this.setState({
        timer: setTimeout(() => {
          fetchCourses(query);
        }, interval),
      });
    }
  }

  addClass(courses, course = null) {
    const { addToSchedule } = this.props;
    addToSchedule(courses, course);
  }

  showSearch() {
    this.setState({ showSearch: true });
  }

  hideSearch() {
    this.setState({ showSearch: false });
  }

  render() {
    const { courses } = this.props;
    const { showSearch } = this.state;
    return (
      <div id="generator-search" className="no-print">
        <FormModule
          name="class_search"
          type="input"
          onChange={(e, value) => this.searchOnChange(value)}
          onFocus={this.showSearch}
          placeholder="Search a Course (PHYS 2305, CS 1054), CRN (12345), or Course Name (Foundations of Physics, Intro to Programming)"
        />
        { showSearch && (
          <ul className="list-group">
            {(courses && Object.keys(courses).length > 0) ? Object.keys(courses).map(key => (
              <li className="list-group-item" key={key}>
                <div className="btn-group">
                  <button
                    type="button"
                    className="btn btn-default btn-sm"
                    onClick={() => {
                      this.addClass(courses[key]);
                      this.hideSearch();
                    }}
                  >
                    {' '}
                    Add
                  </button>
                  <button type="button" className="btn btn-default btn-sm dropdown-toggle" data-toggle="dropdown">
                    <span className="caret" />
                  </button>
                  <ul className="dropdown-menu">
                    { courses[key].map(course => (
                      <li key={course.crn}>
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
                          onClick={() => {
                            this.addClass(courses[key], course);
                            this.hideSearch();
                          }}
                        >
                          {' '}
                          {course.crn}
                        </a>
                      </li>
                    ))}
                  </ul>
                </div>
                {` ${courses[key][0].subject} ${courses[key][0].courseNumber} - ${courses[key][0].name}`}
              </li>
            )) : <div />}
          </ul>
        )}
      </div>
    );
  }
}

export default SearchList;
