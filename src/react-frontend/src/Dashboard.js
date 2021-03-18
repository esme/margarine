import { useState } from 'react';
import { DataGrid } from '@material-ui/data-grid';
import { makeStyles, withStyles } from '@material-ui/core/styles';
import Graph from './Graph';

const useStyles = makeStyles({
  root: {
    '& .grid-theme': {
      fontFamily: 'Ebrima',
    },
    '& .header-theme': {
      backgroundColor: 'black',
      color: 'white'
    },
    '& .cell-theme': {
      backgroundColor: 'rgba(255, 255, 255, 0.3)'
    },
  },
});

function Dashboard () {
  const classes = useStyles();

  const [shortUrl, setShortUrl] = useState("");
  const [company, setCompany] = useState("");

  const columns = [
    { 
      field: 'shortUrl',
      headerClassName: 'grid-theme header-theme',
      cellClassName: 'grid-theme cell-theme',
      headerName: 'Short link',
      width: 420
    },
    { 
      field: 'originalUrl',
      headerClassName: 'grid-theme header-theme',
      cellClassName: 'grid-theme cell-theme',
      headerName: 'Original Link',
      width: 420
    },
    {
      field: 'dateCreated',
      headerClassName: 'grid-theme header-theme',
      cellClassName: 'grid-theme cell-theme',
      headerName: 'Date Created',
      type: 'date',
      width: '100%',
    }
  ];

  const rows = [
    { id: 1, shortUrl: 'https://en.wikipedia.org/wiki/Snow', originalUrl: 'https://en.wikipedia.org/wiki/Jon', dateCreated: Date.now() },
    { id: 2, shortUrl: 'https://en.wikipedia.org/wiki/Lannister', originalUrl: 'https://en.wikipedia.org/wiki/Cersei', dateCreated: Date.now() },
    { id: 3, shortUrl: 'https://en.wikipedia.org/wiki/Lannister', originalUrl: 'https://en.wikipedia.org/wiki/Jaime', dateCreated: Date.now() },
    { id: 4, shortUrl: 'https://en.wikipedia.org/wiki/Stark', originalUrl: 'https://en.wikipedia.org/wiki/Arya', dateCreated: Date.now() },
    { id: 5, shortUrl: 'https://en.wikipedia.org/wiki/Targaryen', originalUrl: 'https://en.wikipedia.org/wiki/Daenerys', dateCreated: Date.now() },
    { id: 6, shortUrl: 'https://en.wikipedia.org/wiki/Melisandre', originalUrl: null, dateCreated: Date.now() },
    { id: 7, shortUrl: 'https://en.wikipedia.org/wiki/Clifford', originalUrl: 'https://en.wikipedia.org/wiki/Ferrara', dateCreated: Date.now() },
    { id: 8, shortUrl: 'https://en.wikipedia.org/wiki/Frances', originalUrl: 'https://en.wikipedia.org/wiki/Rossini', dateCreated: Date.now() },
    { id: 9, shortUrl: 'https://en.wikipedia.org/wiki/Roxie', originalUrl: 'https://en.wikipedia.org/wiki/Harvey', dateCreated: Date.now() },
  ];

  return (
    <>
      <h1>Dashboard</h1>
      <div className="form">
        <div>Track your URL:</div>
        <input
          className="inputRounded inputText"
          value={shortUrl}
          type="text"
          onChange={e => setShortUrl(e.target.value)}
        />
        <br/>
        <div>Search by Company Name:</div>
        <input
          className="inputRounded inputText"
          value={company}
          type="text"
          onChange={e => setCompany(e.target.value)}
        />
      </div>
      <div style={{ height: 400, width: '80%', margin: '25px auto 0 auto' }} className={classes.root}>
        <DataGrid rows={rows} columns={columns} pageSize={5} />
      </div>
      <div className="card-container">
        <div className="card">
          <div className="container">
            <h4>Total Unique Visitors</h4>
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
            <h4>Visitors Today</h4>
            <h1>12</h1>
          </div>
        </div>
      </div>
      <Graph />
    </>
  );
}

export default Dashboard;
