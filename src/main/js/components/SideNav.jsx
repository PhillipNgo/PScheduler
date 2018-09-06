import React from 'react';
import {
  Switch,
  Route,
  NavLink,
  Link,
} from 'react-router-dom';

class SideNav extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      minimized: false,
    };
  }

  changeSize() {
    this.setState(({ minimized }) => ({
      minimized: !minimized,
    }));
  }

  render() {
    const { minimized } = this.state;
    const { menu } = this.props;
    return (
      <div id="side-nav" className={`no-print ${minimized ? 'side-nav-min' : ''}`}>
        <a
          id="nav-button"
          onClick={() => this.changeSize()}
          className="w"
        >
          <span className={`glyphicon glyphicon-menu-${minimized ? 'right' : 'left'}`} />
        </a>
        <nav>
          <Switch>
            { menu.map(tab => (
              <Route key={`${tab.title}.subMenu`} path={tab.path}>
                <ul id="menu-secondary" className="menu">
                  { tab.subMenu.map(subTab => (
                    <li key={`${tab.title}.${subTab.title}`}>
                      <NavLink
                        to={{
                          pathname: tab.path,
                          hash: subTab.hash,
                        }}
                        isActive={(match, location) => location.hash === subTab.hash}
                      >
                        {subTab.title}
                      </NavLink>
                    </li>
                  ))}
                </ul>
              </Route>
            ))}
          </Switch>
        </nav>
        <nav className="bottom">
          <ul id="menu-primary" className="menu">
            { menu.map(tab => (
              <li key={tab.title}>
                <NavLink
                  to={{
                    pathname: tab.path,
                    hash: tab.subMenu.length > 0 ? tab.subMenu[0].hash : '',
                  }}
                >
                  {tab.title}
                </NavLink>
              </li>
            ))}
          </ul>
          <Link to="/" id="logo" href="/" className={minimized ? 'logo-min' : ''} />
        </nav>
      </div>
    );
  }
}

export default SideNav;
