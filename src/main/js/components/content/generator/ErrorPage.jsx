import React from 'react';

const ErrorPage = () => (
  <div className="page-loader">
    <h3>
      Something went wrong generating your search!
    </h3>
    <br />
    <h4>
      {'If the problem persists, please submit an issue on '}
      <a href="https://github.com/PhillipNgo/PScheduler">
        GitHub
      </a>
      {' or email me the details at '}
      <a href="mailto:ngophill@vt.edu?Subject=PScheduler">
        ngophill@vt.edu
      </a>
    </h4>
    <br />
    <br />
    <div className="border-top pad-top">
      <h2>
        Recent Changes
      </h2>
      <br />
      <h4 className="pad-bottom">
        [10/15/2019] Spring 2019 Courses have been removed
      </h4>
      <h4>
        [2/6/2019] Generation links created before this date will no longer work,
        please generate your schedule again for a new link
      </h4>
    </div>
  </div>
);

export default ErrorPage;
