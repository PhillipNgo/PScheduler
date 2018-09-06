import React from 'react';
import { ClipLoader } from 'react-spinners';
import FormModule from '../../FormModule';
import CourseTable from '../../CourseTable';
import formDefaults from '../../../constants/formDefaults';

class Search extends React.Component {
  constructor(props) {
    super(props);
    const { initialize } = props;
    initialize({
      term: formDefaults.termValue,
    });
    this.state = {
      timer: null,
      interval: 750,
    };
  }

  componentDidMount() {
    $('.selectpicker').selectpicker('refresh');
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

  render() {
    const { courses, isFetching } = this.props;
    const flatCourses = [];
    Object.keys(courses).forEach((key) => {
      courses[key].forEach((course) => {
        flatCourses.push(course);
      });
    });
    return (
      <form id="generator-search" className="pad-top">
        <div className="pad-bottom">
          <FormModule
            type="select"
            name="term"
            width="auto"
            values={formDefaults.terms}
          />
        </div>
        <FormModule
          name="class_search"
          type="input"
          onChange={(e, value) => this.searchOnChange(value)}
          onFocus={this.showSearch}
          placeholder="Search a Course (PHYS 2305, CS 1054) or Course Name (Foundations of Physics, Intro to Programming)"
        />
        {
          isFetching ? (
            <div className="page-loader">
              <h1>
                Loading Results
              </h1>
              <ClipLoader size={200} color="darkorange" />
            </div>
          ) : (
            <CourseTable courses={flatCourses} header />
          )
        }
      </form>
    );
  }
}

export default Search;
