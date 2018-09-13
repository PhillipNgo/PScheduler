import React from 'react';
import { Redirect } from 'react-router-dom';
import { ClipLoader } from 'react-spinners';
import SearchForm from '../../containers/content/generator/SearchForm';
import Schedules from '../../containers/content/generator/Schedules';
import TimetableResults from '../../containers/content/generator/TimetableResults';

const Generator = ({ isGenerating, location }) => {
  if (isGenerating) {
    return <Generator.LoadingScreen />;
  }
  switch (location.hash) {
    case '#schedules': return <Schedules />;
    case '#timetable': return <TimetableResults />;
    case '#search': return <SearchForm />;
    default: return <Redirect to={{ pathname: '/generator', hash: '#search' }} />;
  }
};

Generator.LoadingScreen = () => (
  <div className="page-loader">
    <h1>
      Generating schedules...
    </h1>
    <ClipLoader size={200} color="darkorange" />
  </div>
);

export default Generator;
