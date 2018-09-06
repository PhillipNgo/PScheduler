import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => (
  <div className="home">
    <Link to="/about" className="contact glyph">
      Contact
    </Link>
    <div className="jumbotron">
      <h1>
        <span className="o">
          P
        </span>
        Scheduler
      </h1>
      <h2>
        Virginia Tech Schedule Generation and Course Lookup
      </h2>
      <div className="row">
        <Glyph
          glyphicon="list-alt"
          description="Course Timetable"
          link="/timetable"
          style={{
            marginLeft: '-3%',
          }}
        />
        <Glyph
          glyphicon="calendar"
          description="Schedule Generation"
          link="/generator"
          hash="#search"
          style={{
            marginRight: '-3%',
          }}
        />
        <Glyph
          glyphicon="hand-up"
          description="Schedule Builder"
          link="/builder"
        />
      </div>
    </div>
  </div>
);

const Glyph = ({
  glyphicon,
  description,
  link,
  hash = '',
  style,
}) => (
  <div className="col-sm-4 pad-top-large">
    <Link
      to={{
        pathname: link,
        hash,
      }}
      className="glyph"
    >
      <span style={style} className={`glyphicon glyphicon-${glyphicon}`} />
      <h3 className="pad-top">
        {description}
      </h3>
    </Link>
  </div>
);

export default Home;
