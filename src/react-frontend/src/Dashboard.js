import axios from 'axios';
import { useReducer, useState } from 'react';
import Graph from './Graph';
import { REST_API_URL } from './api'

function Dashboard () {
  let [shortUrl, setShortUrl] = useState("");
  const [company, setCompany] = useState("");
  const [margarineLink, setMargarineLink] = useState("https://margarine.com/Jon");
  const [originalLink, setOriginalLink] = useState("https://en.wikipedia.org/wiki/Jon");
  const [dateCreated, setDateCreated] = useState("11-04-2020");

  const add = async () => {
    const res = await getMetrics();
    console.log('res', res);

    setMargarineLink(res.data.shortUrl);
    res.data === 'NOT_FOUND' ? setOriginalLink('NOT FOUND') : setOriginalLink(res.data.originalUrl);
    res.data.dateCreated ? setDateCreated(res.data.dateCreated) : setDateCreated("N/A");
  }

  const getMetrics = async () => {
    console.log(shortUrl);
    if (shortUrl.includes('/')) {
      shortUrl = shortUrl.split('/')[-1];
    }
    const res = await axios.get(`${REST_API_URL}/get?short_url=${shortUrl}`);
    return res;
  }

  return (
    <>
      <div className="form-container">
        <div className="form">
          <div style={{height: '44px'}}></div>
          <div className="form-text">Track your URL:</div>
          <div>
            <input
              className="inputRounded inputText"
              style={{width: '65%'}}
              value={shortUrl}
              type="text"
              onChange={e => setShortUrl(e.target.value)}
            />
            <input
              className="inputRounded inputButton"
              style={{width: '25%'}}
              value="Add"
              type="button"
              onClick={add}
            />
          </div>
          <div className="form-text"
            style={{margin: '20px 25px'}}
          >
            OR
          </div>
          <div className="form-text">Search by Company Name:</div>
          <div>
            <input
              className="inputRounded inputText"
              style={{width: '65%'}}
              value={company}
              type="text"
              onChange={e => setCompany(e.target.value)}
              placeholder="Coming soon..."
              disabled
            />
            <input
              className="inputRounded inputButton"
              style={{width: '25%'}}
              value="Search"
              type="button"
              disabled
            />
          </div>
        </div>
        <div className="border"></div>
        <div className="dashboard">
          <h1 style={{textAlign: 'left', marginLeft: '10%'}}>Dashboard</h1>
          <table id="links-table">
            <tr>
              <th style={{borderTopLeftRadius: '10px'}}>Margarine Link</th>
              <th>Original Link</th>
              <th style={{borderTopRightRadius: '10px'}}>Date Created</th>
            </tr>
            <tr>
              <td>{margarineLink}</td>
              <td>{originalLink}</td>
              <td>{dateCreated}</td>
            </tr>
          </table>
          <div className="card-container">
            <div className="card">
              <div className="container">
                <h4>Total Clicks</h4>
                <h1>786</h1>
              </div>
            </div>
            <div className="card">
              <div className="container">
                <h4>Date Last Accessed</h4>
                <h1>2/24/21</h1>
              </div>
            </div>
            <div className="card">
              <div className="container">
                <h4>Most Visitors From</h4>
                <h1>MA</h1>
                <p>USA</p>
              </div>
            </div>
            <div className="card">
              <div className="container">
                <h4>Clicks Today</h4>
                <h1>12</h1>
              </div>
            </div>
          </div>
          <Graph />
        </div>
      </div>
    </>
  );
}

export default Dashboard;
