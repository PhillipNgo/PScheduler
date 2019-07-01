import React from 'react';
import { Switch, Route } from 'react-router-dom';
import Home from './Home';
import SideNav from './SideNav';
import ContentPage from './ContentPage';
import menu from '../constants/menu';
import MaintenancePage from './MaintenancePage';

const App = ({ maintenance }) => (
  maintenance
    ? <Route path="/" component={MaintenancePage} /> : (
      <Switch>
        <Route path={`(${menu.map(tab => tab.path).join('|')})`}>
          <div id="content">
            <SideNav menu={menu} />
            <ContentPage menu={menu} />
          </div>
        </Route>
        <Route path="/" component={Home} />
      </Switch>
    )
);

export default App;
