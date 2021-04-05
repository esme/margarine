import axios from 'axios';
import { useState } from 'react';
import Graph from './Graph';

function Dashboard () {
  const REST_API_URL = 'http://localhost:8080';

  const [shortUrl, setShortUrl] = useState("");
  const [company, setCompany] = useState("");
  const sendShortUrl = async () => {
    const res = await axios.get(REST_API_URL);
    console.log(res.data.content);
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
              onClick={sendShortUrl}
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
              <td>https://en.wikipedia.org/wiki/Snow</td>
              <td>https://en.wikipedia.org/wiki/Jon</td>
              <td>11-11-2001</td>
            </tr>
          </table>
          {/* <div style={{ height: 400, width: '80%', margin: '25px auto 0 auto' }} className={classes.root}>
            <DataGrid rows={rows} columns={columns} pageSize={5} />
          </div> */}
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
