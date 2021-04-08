import './App.css';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import Dashboard from './Dashboard';
import Shortener from './Shortener';
import Background from './images/margarinebg.jpg';
import Logo from './images/margarinelogo.png';
import Graph from './Graph';
import UserTracking from './UserTracking';

function Header(props) {
  return (
    <div className="header">
      <div
        style={{"width": "100px"}}
      ></div>
      <img alt="logo" src={Logo}>
      </img>
      <div
        style={{"width": "100px"}}
      >
        {props.children}
      </div>
    </div>
  );
}

function App() {
  return (
    <div className="App">
      <div className="bg"
        style={{
          backgroundImage: `url(${Background})`,
        }}
      >
        <BrowserRouter>
          <Switch>
            <Route exact path="/dashboard">
              <Header>
                <input
                  className="inputRounded inputButton inputDashboard"
                  value="Homepage"
                  type="button"
                  onClick={() => window.location.href="/"}
                />
              </Header>
              <Dashboard />
            </Route>
            <Route exact path="/">
              <Header>
                <input
                  className="inputRounded inputButton inputDashboard"
                  value="Dashboard"
                  type="button"
                  onClick={() => window.location.href="/dashboard"}
                />
              </Header>
              <Shortener />
            </Route>
            <Route path="/">
              <Header>
                <input
                  className="inputRounded inputButton inputDashboard"
                  value="Homepage"
                  type="button"
                  onClick={() => window.location.href="/"}
                />
              </Header>
              <UserTracking />
            </Route>
          </Switch>
        </BrowserRouter>
      </div>
    </div>
  );
}

export default App;
