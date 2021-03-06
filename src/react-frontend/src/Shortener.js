import axios from 'axios';
import { useState, useRef } from 'react';
import { REST_API_URL } from './api';

const Shortener = () => {
  const [originalUrl, setOriginalUrl] = useState("");
  const [company, setCompany] = useState("");
  const [customUrl, setCustomUrl] = useState("");
  const [shortenedUrl, setShortenedUrl] = useState("");
  const [successMessage, setSuccessMessage] = useState("");
  const inputRef = useRef(null);
  
  const generateLink = async () => {
    if (originalUrl.length) {
      const res = await axios.post(`${REST_API_URL}/generate`, {
        originalUrl,
        company,
        shortUrl: customUrl
      });
      console.log(res);
      if (res.data === 'CONFLICT') {
        setSuccessMessage("Error! Your URL already exists in database.");
      } else if (res.data === 'ALREADY_REPORTED') {
        setSuccessMessage("Error! Your customized URL already exists in database.");
      } else {
        setSuccessMessage("Success!");
        setShortenedUrl(res.data.short_url);
      }
    }
  }

  const resetFields = () => {
    setOriginalUrl("");
    setCompany("");
    setCustomUrl("");
    setShortenedUrl("");
    setSuccessMessage("");
  }

  const copyToClipboard = (e) => {
    inputRef.current.select()
    document.execCommand("copy")
    e.target.focus();
    console.log(`Copied ${inputRef.current.value}!`)
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
          value={originalUrl}
          type="text"
          onChange={e => setOriginalUrl(e.target.value)}
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
        <div
          className="formText"
          style={{marginTop: '30px'}}
        >
          Customize your link:
        </div>
        <div>
          <input
            style={{width: '30%'}}
            className="inputRounded inputText"
            value={REST_API_URL}
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
          style={{width: '120px', marginBottom: '30px'}}
          value="Shorten"
          type="button"
          onClick={generateLink}
        />
        {successMessage &&
          <div className="successMessage">{successMessage}</div>
        }
        <div className="formText">Get your shortened URL:</div>
        <input
          ref={inputRef}
          className="inputRounded inputText"
          style={{width: '50%'}}
          value={`${REST_API_URL}/${shortenedUrl}`}
          onChange={setShortenedUrl}
          readOnly="readOnly"
          type="text"
        />
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
        <br/>
        <br/>
        <input
          style={{
            backgroundColor: originalUrl.length ? '#ebbb10' : '#FFE48A',
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
