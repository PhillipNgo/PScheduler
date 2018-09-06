let api;

if (process.env.NODE_ENV === 'production') {
  api = require('./apiUrl').default; // eslint-disable-line global-require
} else {
  api = `http://localhost:${devPorts.backend}/api`;
}

const courses = `${api}/courses`;

const courseSearchUrl = `${courses}/search`;

export default courseSearchUrl;
