import React from 'react';

const MaintenancePage = () => (
  <div className="home">
    <div className="contact">
      <a href="https://github.com/PhillipNgo/PScheduler" className="glyph">
        Github
      </a>
      {' / '}
      <a href="mailto:ngophill@vt.edu?Subject=PScheduler" className="glyph">
        Contact
      </a>
    </div>
    <div className="jumbotron">
      <h1>
        <span className="o">
          P
        </span>
        Scheduler
      </h1>
      <h2>
        Currently down for an update!
      </h2>
      <h2>
        We&#39;ll be back before the next course request period
      </h2>
    </div>
  </div>
);

export default MaintenancePage;
