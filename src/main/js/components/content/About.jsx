import React from 'react';

const About = () => (
  <div id="about">
    <div className="page-header o">
      <h1>
        Contact
      </h1>
    </div>
    <p>
      Thanks for using the site!
    </p>
    <p>
      If you run into any problems or have any ideas I could implement,
      feel free to email me or submit an issue on github!
    </p>
    <p>
      <a href="mailto:ngophill@vt.edu?Subject=PScheduler">
        ngophill@vt.edu
      </a>
      <br />
      <a href="https://github.com/PhillipNgo/PScheduler">
        https://github.com/PhillipNgo/PScheduler
      </a>
    </p>
    <div className="page-header o">
      <h1>
        Rewritten!
      </h1>
    </div>
    <p>
      PScheduler has been completeley rewritten from the ground up! Performance should
      be minimized as the site is now static and no longer relies on a server to generate HTML.
    </p>
    <p>
      If you&#39;re a developer feel free to fork the repository and make a pull request!
      I&#39;ve made it easy to set up and the code much cleaner.
    </p>
    <h3>
      Technologies
    </h3>
    <p>
      PScheduler is now built on some of the following libraries / technologies!
    </p>
    <b>
      Front end
    </b>
    <ul>
      <li>
        <a href="https://reactjs.org/">
          React
        </a>
        <ul>
          <li>
            <a href="https://reacttraining.com/react-router/">
              React-Router
            </a>
          </li>
        </ul>
      </li>
      <li>
        <a href="https://redux.js.org/">
          Redux
        </a>
        {' / '}
        <a href="https://github.com/reduxjs/react-redux">
          React-Redux
        </a>
        <ul>
          <li>
            <a href="https://redux-form.com">
              Redux-Form
            </a>
          </li>
          <li>
            <a href="https://github.com/reduxjs/redux-thunk">
              Redux-Thunk
            </a>
          </li>
        </ul>
      </li>
      <li>
        <a href="https://webpack.js.org/">
          Webpack
        </a>
      </li>
      <li>
        <a href="https://getbootstrap.com/docs/3.3/">
          Bootstrap
        </a>
        <ul>
          <li>
            <a href="https://developer.snapappointments.com/bootstrap-select/">
              Bootstrap Select
            </a>
          </li>
        </ul>
      </li>
    </ul>
    <b>
      Back end
    </b>
    <ul>
      <li>
        <a href="https://aws.amazon.com">
          AWS
        </a>
        <ul>
          <li>
            <a href="https://aws.amazon.com/dynamodb/">
              DynamoDB
            </a>
          </li>
          <li>
            <a href="https://aws.amazon.com/lambda/">
              Lambda
            </a>
            {' (Java)'}
          </li>
          <li>
            <a href="https://aws.amazon.com/api-gateway/">
              API Gateway
            </a>
          </li>
          <li>
            <a href="https://aws.amazon.com/s3/">
              S3
            </a>
          </li>
          <li>
            <a href="https://aws.amazon.com/route53/">
              Route 53
            </a>
          </li>
        </ul>
      </li>
      <li>
        <a href="http://htmlunit.sourceforge.net/">
          HtmlUnit
        </a>
      </li>
      <li>
        <a href="https://spring.io/projects/spring-boot">
          Spring Boot
        </a>
        {' (for development)'}
        <ul>
          <li>
            <a href="http://hibernate.org/">
              Hibernate
            </a>
          </li>
          <li>
            <a href="http://www.h2database.com/html/main.html">
              H2
            </a>
          </li>
        </ul>
      </li>
    </ul>
  </div>
);

export default About;
