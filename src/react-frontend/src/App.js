import './App.css';
import axios from 'axios';
import { useState } from 'react';

const REST_API_URL = 'http://localhost:8080/greeting';

const Shortener = () => {
  const [data, setData] = useState('Margarine');

  const fetchData = async () => {
    const res = await axios.get(REST_API_URL);
    setData(res.data.content);
  }

  return (
    <>
      <h1>{data}</h1>
      <input
        type="button"
        value="Get data"
        onClick={fetchData}
      ></input>
    </>
  );
}

function App() {
  return (
    <div className="App">
      <Shortener />
    </div>
  );
}

export default App;
