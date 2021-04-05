import axios from 'axios';
import { useState, useRef } from 'react';


const Shortener = () => {
  const REST_API_URL = 'http://localhost:8080';

  const [longUrl, setLongUrl] = useState("");
  const [company, setCompany] = useState("");
  const [customUrl, setCustomUrl] = useState("");
  const inputRef = useRef(null);
  
  const sendData = async () => {
    const res = await axios.get(REST_API_URL);
    console.log(res.data.content);
  }

  const resetFields = () => {
    setLongUrl("");
    setCompany("");
    setCustomUrl("");
  }

  const copyToClipboard = (e) => {
    console.log(inputRef.current)
    inputRef.current.select()
    document.execCommand("copy")
    e.target.focus();
    console.log('Copied!')
  }

  return (
    <>
      <h1 className="headerText">Welcome to Margarine!</h1>
      <div>Start shortening your links with the form below:</div>
      <div className="form">
        <input
          className="inputRounded inputText"
          style={{width: '60%'}}
          placeholder="Enter your URL"
          value={longUrl}
          type="text"
          onChange={e => setLongUrl(e.target.value)}
        />
        <br />
        <input
          className="inputRounded inputText"
          style={{width: '60%'}}
          placeholder="Your Company Name"
          value={company}
          type="text"
          onChange={e => setCompany(e.target.value)}
        />
        <br />
        <div className="formText">Customize your link:</div>
        <div>
          <input
            style={{width: '30%'}}
            className="inputRounded inputText"
            value="margarine.com/"
            type="text"
            disabled
          />
          <input
            className="inputRounded inputText"
            style={{width: '30%'}}
            placeholder=""
            value={customUrl}
            type="text"
            onChange={e => setCustomUrl(e.target.value)}
          />
        </div>
        <br />
        <input
          className="inputRounded inputButton"
          style={{width: '120px'}}
          value="Shorten"
          type="button"
          onClick={sendData}
        />
        <br />
        <div className="formText">Get your shortened URL:</div>
        <input
          ref={inputRef}
          className="inputRounded inputText"
          style={{width: '50%'}}
          value="margarine.com/123"
          type="text"
          readonly="readonly"
        />
        <span>
          <input
            className="inputRounded inputButton"
            style={{
              fontFamily: 'fontAwesome',
              fontSize: 15,
              color: '#fff'
            }}
            value="&#xf0c5;"
            type="button"
            onClick={copyToClipboard}
          />
        </span>
        <br/>
        <br/>
        <input
          style={{
            backgroundColor: longUrl.length ? '#ebbb10' : '#FFE48A',
            width: '120px'
          }}
          className="inputRounded inputButton"
          value="Do Another"
          type="button"
          onClick={resetFields}
        />
      </div>
    </>
  );
}

export default Shortener;
