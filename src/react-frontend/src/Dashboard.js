import { useState } from 'react';
import TextField from '@material-ui/core/TextField';
import { DataGrid } from '@material-ui/data-grid';

function Dashboard () {
  const [shortUrl, setShortUrl] = useState("");
  const [company, setCompany] = useState("");

  const columns = [
    { field: 'id', headerName: 'ID', width: 70 },
    { field: 'company', headerName: 'Company', width: 130 },
    { field: 'url', headerName: 'URL', width: 480 },
    {
      field: 'clicks',
      headerName: 'Number of Clicks',
      type: 'number',
      width: 180,
    }
  ];

  const rows = [
    { id: 1, company: 'Snow', url: 'https://en.wikipedia.org/wiki/Jon', clicks: 35 },
    { id: 2, company: 'Lannister', url: 'https://en.wikipedia.org/wiki/Cersei', clicks: 42 },
    { id: 3, company: 'Lannister', url: 'https://en.wikipedia.org/wiki/Jaime', clicks: 45 },
    { id: 4, company: 'Stark', url: 'https://en.wikipedia.org/wiki/Arya', clicks: 16 },
    { id: 5, company: 'Targaryen', url: 'https://en.wikipedia.org/wiki/Daenerys', clicks: null },
    { id: 6, company: 'Melisandre', url: null, clicks: 150 },
    { id: 7, company: 'Clifford', url: 'https://en.wikipedia.org/wiki/Ferrara', clicks: 44 },
    { id: 8, company: 'Frances', url: 'https://en.wikipedia.org/wiki/Rossini', clicks: 36 },
    { id: 9, company: 'Roxie', url: 'https://en.wikipedia.org/wiki/Harvey', clicks: 65 },
  ];

  return (
    <>
      <h1>Dashboard</h1>
      <TextField
        id="outlined-basic"
        label="Enter short URL"
        variant="outlined"
        value={shortUrl}
        type="text"
        onChange={e => setShortUrl(e.target.value)}
      ></TextField>
      <br/>
      <TextField
        id="outlined-basic"
        label="Enter company"
        variant="outlined"
        value={company}
        type="text"
        onChange={e => setCompany(e.target.value)}
      ></TextField>
      <br/>
      <div style={{ height: 400, width: '100%' }}>
        <DataGrid rows={rows} columns={columns} pageSize={5} checkboxSelection />
      </div>
    </>
  );
}

export default Dashboard;
