import React from 'react';
import ReactDOM from 'react-dom';
import { HashRouter as Router } from 'react-router-dom';
import { createStore, applyMiddleware } from 'redux';
import { Provider } from 'react-redux';
import thunk from 'redux-thunk';
import reducer from './reducers/reducer';
import App from './components/App';

/* eslint-disable global-require, import/no-extraneous-dependencies */
let middleware = [thunk];
if (process.env.NODE_ENV !== 'production') {
  const logger = require('redux-logger').createLogger({});
  middleware = [...middleware, logger];
}
const store = createStore(reducer, applyMiddleware(...middleware));

ReactDOM.render(
  <Provider store={store}>
    <Router>
      <App maintenance={false} />
    </Router>
  </Provider>,
  document.getElementById('root'),
);
