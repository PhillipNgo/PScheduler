import React from 'react';
import { ClipLoader } from 'react-spinners';
import SearchForm from '../../containers/content/generator/SearchForm';
import Schedules from '../../containers/content/generator/Schedules';
import TimetableResults from '../../containers/content/generator/TimetableResults';

const Generator = ({ isGenerating, location }) => {
  switch (location.hash) {
    case '#search': return <SearchForm />;
    case '#schedules': return isGenerating ? <Generator.LoadingScreen /> : <Schedules />;
    case '#timetable': return isGenerating ? <Generator.LoadingScreen /> : <TimetableResults />;
    default: return (
      <div>
        Error
      </div>
    ); // should make an error page
  }
}

Generator.LoadingScreen = () => (
  <div className="page-loader">
    <h1>
      Generating schedules...
    </h1>
    <ClipLoader size={200} color="darkorange" />
  </div>
);

export default Generator;
