import axios from 'axios';
import { useState } from 'react';
import moment from 'moment';
import Graph from './Graph';
import { REST_API_URL } from './api'

const coords = [
  { "state": "CA", "latitude": 36.778259, "longitude": -119.417931, "timeClicked": moment().format("YYYY-MM-DD[T]HH:mm:ss")},
  { "state": "CA", "latitude": 36.8259, "longitude": -119.31, "timeClicked": "2021-03-09T12:52:03" },
  { "state": "CA", "latitude": 36.259, "longitude": -119.7931, "timeClicked": "2021-02-09T12:52:03" },
  { "state": "MA", "latitude": 42.4072, "longitude": -71.3824, "timeClicked": "2021-01-09T12:52:03" },
  { "state": "MA", "latitude": 42.2, "longitude": -71.824, "timeClicked": "2021-01-09T12:52:03" },
  { "state": "MA", "latitude": 42.72, "longitude": -71.24, "timeClicked": "2021-03-09T12:52:03" },
  { "state": "NY", "latitude": 40.74072, "longitude": -74.03824, "timeClicked": "2021-02-09T12:52:03" },
];

function Dashboard () {
  let [shortUrl, setShortUrl] = useState("");
  const [company, setCompany] = useState("");
  const [margarineLink, setMargarineLink] = useState(`${REST_API_URL}/goo123`);
  const [originalLink, setOriginalLink] = useState("https://google.com");
  const [dateCreated, setDateCreated] = useState(moment().format('MM-DD-YYYY'));
  const [totalClicks, setTotalClicks] = useState(coords.length);
  const [dateLastAccessed, setDateLastAccessed] = useState(moment(new Date(coords[0].timeClicked)).format('MM/DD/YY'));
  const [mostVisitorsFrom, setMostVisitorsFrom] = useState("MA");
  const [clicksToday, setClicksToday] = useState(1);
  const [coordinates, setCoordinates] = useState(coords);

  const add = () => {
    cleanUpShortUrl();
    addTableMetrics();
    addCardMetrics();
    addGraphMetrics();
  }

  const cleanUpShortUrl = () => {
    if (shortUrl.includes('/')) {
      shortUrl = shortUrl.split('/')[-1];
    }
  }

  const addCardMetrics = async () => {
    const res = await axios.get(`${REST_API_URL}/get/${shortUrl}`);
    console.log('card data: ', res);
    
    if (res.data !== 'NOT_FOUND') {
      setTotalClicks(res.data.numberOfClicks);
      setMostVisitorsFrom(res.data.mostVisitorsFrom);
      let dateFromData = moment(new Date(res.data.dateLastAccessed)).format('MM/DD/YY');
      if (dateFromData === 'Invalid Date') {
        dateFromData = 'N/A';
      }
      setDateLastAccessed(dateFromData);
      setClicksToday(res.data.numberOfVisitorsToday);
    }
  }

  const addTableMetrics = async () => {
    const res = await axios.get(`${REST_API_URL}/get?short_url=${shortUrl}`);
    console.log('table data: ', res);

    setMargarineLink(`${REST_API_URL}/${shortUrl}`);
    if (res.data !== 'NOT_FOUND') {
      setOriginalLink(res.data.originalUrl)
      const dateFromData = res.data.dateCreated ? moment(new Date(res.data.dateCreated)).format('MM-DD-YY') : 'N/A';
      setDateCreated(dateFromData);
    } else {
      setOriginalLink('NOT FOUND!');
    }
  }

  const addGraphMetrics = async () => {
    const res = await axios.get(`${REST_API_URL}/get/${shortUrl}/coordinates`);
    console.log('graph data: ', res);
    setCoordinates(res.data.coordinates);
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
            <thead>
              <tr>
                <th style={{borderTopLeftRadius: '10px'}}>Margarine Link</th>
                <th>Original Link</th>
                <th style={{borderTopRightRadius: '10px'}}>Date Created</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{margarineLink}</td>
                <td>{originalLink}</td>
                <td>{dateCreated}</td>
              </tr>
            </tbody>
          </table>
          <div className="card-container">
            <div className="card">
              <div className="container">
                <h4>Total Clicks</h4>
                <h1>{totalClicks}</h1>
              </div>
            </div>
            <div className="card">
              <div className="container">
                <h4>Date Last Accessed</h4>
                <h1>{dateLastAccessed}</h1>
              </div>
            </div>
            <div className="card">
              <div className="container">
                <h4>Most Visitors From</h4>
                <h1>{mostVisitorsFrom}</h1>
                <p>USA</p>
              </div>
            </div>
            <div className="card">
              <div className="container">
                <h4>Clicks Today</h4>
                <h1>{clicksToday}</h1>
              </div>
            </div>
          </div>
          <Graph coordinates={coordinates} />
        </div>
      </div>
    </>
  );
}

export default Dashboard;
