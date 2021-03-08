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
      <h1 className="headerText">Welcome to Margarine!</h1>
      <p>Start shortening your links with the form below:</p>
      <div className="form">
        <input
          className="inputRounded inputText"
          placeholder="Enter your URL"
          value={longUrl}
          type="text"
          onChange={e => setLongUrl(e.target.value)}
        />
        <br />
        <input
          className="inputRounded inputText"
          placeholder="Your Company Name"
          value={company}
          type="text"
          onChange={e => setCompany(e.target.value)}
        />
        <br />
        <p>Customize your link:</p>
        <div
          style={{display: 'flex'}}
        >
          <input
            style={{marginRight: '20px'}}
            className="inputRounded inputText"
            value="margarine.com"
            type="text"
            disabled
          />
          <input
            className="inputRounded inputText"
            placeholder=""
            value={customUrl}
            type="text"
            onChange={e => setCustomUrl(e.target.value)}
          />
        </div>
        <br />
        <input
          className="inputRounded inputButton"
          value="Shorten"
          type="button"
          onClick={sendData}
        />
        <p>Get your shortened URL:</p>
        <input
          className="inputRounded inputText"
          value="margarine.com/123"
          type="text"
          disabled
        />
        <br/>
        <br/>
        <input
          style={{backgroundColor: '#FFE48A'}}
          className="inputRounded inputButton"
          value="Do Another"
          type="button"
          onClick={sendData}
        />
      </div>
    </>
  );
}

export default Shortener;
