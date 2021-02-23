import axios from 'axios';
import { useState } from 'react';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';


const Shortener = () => {
  const REST_API_URL = 'http://localhost:8080/greeting';

  const [longUrl, setLongUrl] = useState("");
  const [company, setCompany] = useState("");
  
  const sendData = async () => {
    const res = await axios.get(REST_API_URL);
    console.log(res.data.content);
  }

  return (
    <>
      <h1>Margarine</h1>
      <TextField
        id="outlined-basic"
        label="Enter URL"
        variant="outlined"
        value={longUrl}
        type="text"
        onChange={e => setLongUrl(e.target.value)}
      ></TextField>
      <br/>
      <TextField
        id="outlined-basic"
        label="Enter Company"
        variant="outlined"
        value={company}
        type="text"
        onChange={e => setCompany(e.target.value)}
      ></TextField>
      <br/>
      <Button
        variant="contained"
        color="primary"
        onClick={sendData}
      >Submit</Button>
    </>
  );
}

export default Shortener;
