import './App.css';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import Dashboard from './Dashboard';
import Shortener from './Shortener';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Switch>
          <Route path="/dashboard">
            <Dashboard />
          </Route>
          <Route path="/">
            <Shortener/>
          </Route>
        </Switch>
      </BrowserRouter>
    </div>
  );
}

export default App;
