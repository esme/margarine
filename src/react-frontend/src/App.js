import './App.css';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import Dashboard from './Dashboard';
import Shortener from './Shortener';
import Background from './images/margarinebg.jpg'
import Logo from './images/margarinelogo.png'

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
              <div className="header">
                <img alt="logo" src={Logo}>
                </img>
              </div>
              <Dashboard />
            </Route>
            <Route path="/">
              <div className="header">
                <div></div>
                <img alt="logo" src={Logo}>
                </img>
                <div>
                  <input
                    className="inputRounded inputButton inputDashboard"
                    value="Dashboard"
                    type="button"
                  />
                </div>
              </div>
              <Shortener />
            </Route>
          </Switch>
        </BrowserRouter>
      </div>
    </div>
  );
}

export default App;
