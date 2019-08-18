import React from 'react';
import { Redirect } from 'react-router-dom';
import { ClipLoader } from 'react-spinners';
import SearchForm from '../../containers/content/generator/SearchForm';
import Schedules from '../../containers/content/generator/Schedules';
import TimetableResults from '../../containers/content/generator/TimetableResults';
import ErrorPage from './generator/ErrorPage';

class Generator extends React.Component {
  componentDidMount() {
    const { loadSchedule, location, firstRender } = this.props;

    if (firstRender) {
      loadSchedule(location.search);
    }
  }

  componentDidUpdate(prevProps) {
    const { redirect, redirected } = this.props;

    if (prevProps.redirect && prevProps.redirect === redirect) {
      redirected();
    }
  }

  render() {
    const {
      isGenerating,
      location,
      redirect,
      firstRender,
      error,
    } = this.props;

    if (redirect && location.hash !== redirect.hash) {
      return <Redirect to={redirect} />;
    }

    if (firstRender || isGenerating) {
      return (
        <div className="page-loader">
          <h1>
            Generating schedules...
          </h1>
          <ClipLoader size={200} color="darkorange" />
        </div>
      );
    }

    switch (location.hash) {
      case '#schedules': return error ? <ErrorPage /> : <Schedules />;
      case '#timetable': return error ? <ErrorPage /> : <TimetableResults />;
      case '#search': return <SearchForm />;
      default: return <Redirect to={{ pathname: '/generator', hash: '#search' }} />;
    }
  }
}

export default Generator;
