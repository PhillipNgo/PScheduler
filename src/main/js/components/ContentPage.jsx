import React from 'react';
import { Route, Switch } from 'react-router-dom';

const ContentPage = ({ menu }) => (
  <div id="content-page" className="container-fluid">
    <Switch>
      { menu.map(tab => (
        <Route
          key={tab.path}
          path={tab.path}
          component={tab.component}
        />
      ))}
    </Switch>
  </div>
);

export default ContentPage;
