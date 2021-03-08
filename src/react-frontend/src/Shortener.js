import axios from 'axios';
import { useState } from 'react';

const Shortener = () => {
  const REST_API_URL = 'http://localhost:8080/greeting';

  const [longUrl, setLongUrl] = useState("");
  const [company, setCompany] = useState("");
  const [customUrl, setCustomUrl] = useState("");
  
  const sendData = async () => {
    const res = await axios.get(REST_API_URL);
    console.log(res.data.content);
  }

  return (
    <>
      <h1 className="headerText">Welcome to Margarine</h1>
      <input
        className="inputRounded"
        placeholder="Enter your URL"
        value={longUrl}
        type="text"
        onChange={e => setLongUrl(e.target.value)}
      />
      <br/>
      <input
        className="inputRounded"
        placeholder="Your Company Name"
        value={company}
        type="text"
        onChange={e => setCompany(e.target.value)}
      />
      <br/>
      <p>Customize your link</p>
      <input
        className="inputRounded"
        value="margarine.com"
        type="text"
        disabled
      />
      <input
        className="inputRounded"
        placeholder=""
        value={customUrl}
        type="text"
        onChange={e => setCustomUrl(e.target.value)}
      />
      <br/>
      <p>Get your shortened URL</p>
      <input
        className="inputRounded"
        value="margarine.com/123"
        type="text"
        disabled
      />
      <br/>
      <br/>
      <input
        className="inputRounded inputButton"
        value="Shorten"
        type="button"
        onClick={sendData}
      />
    </>
  );
}

export default Shortener;
