import './App.css';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import Dashboard from './Dashboard';
import Shortener from './Shortener';
import Background from './images/margarinebg.jpg'
import Logo from './images/margarinelogo.png'
import Graph from './Graph';

function Header(props) {
  return (
    <div className="header">
      <div></div>
      <img alt="logo" src={Logo}>
      </img>
      <div>
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
            <Route path="/dashboard">
              <Header>
                <input
                  className="inputRounded inputButton inputDashboard"
                  value="Homepage"
                  type="button"
                />
              </Header>
              <Dashboard />
            </Route>
            <Route path="/">
              <Header>
                <input
                  className="inputRounded inputButton inputDashboard"
                  value="Dashboard"
                  type="button"
                />
              </Header>
              <Shortener />
            </Route>
          </Switch>
        </BrowserRouter>
      </div>
    </div>
  );
}

export default App;
