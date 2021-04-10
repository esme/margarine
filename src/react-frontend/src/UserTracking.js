import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import axios from 'axios';
import moment from 'moment';
import { REST_API_URL } from './api';

const UserTracking = function() {
  let location = useLocation();

  const [message, setMessage] = useState("Checking if URL exists...");
  const [error, setError] = useState(null);
  
  const onError = (error) => {
    console.log(error);
    setError(error.message);
  };
  useEffect(() => {
    const shortUrl = location.pathname.slice(1);
    validateUrl();

    async function validateUrl() {
      console.log('url: ', shortUrl);
      const res = await axios.get(`${REST_API_URL}/get/${shortUrl}`);
      console.log('res: ', res);
      if (res.data === 'NOT_FOUND') {
        setMessage("Page Not Found");
      } else {
        setMessage('Locating...');
        await getPosition();
      }
    }

    async function getPosition() {
      const geo = navigator.geolocation;
      if (!geo) {
        setError('Geolocation is not supported');
      }
      geo.getCurrentPosition(onSuccess, onError);
    }

    async function onSuccess(position) {
      const { latitude, longitude }  = position.coords;
      const { timestamp } = position;
      await generateClick(latitude, longitude, timestamp);
    }

    async function generateClick(latitude, longitude, timestamp) {
      try {
        const data = {
          latitude,
          longitude,
          timeClicked: moment(timestamp).format("YYYY-MM-DD[T]HH:mm:ss")
        };
        console.log('Sending geolocation data: ', data);
        const res = await axios.post(`${REST_API_URL}/click/${shortUrl}`, data);
        console.log(res);
        const str = res.data;
        const i = res.data.indexOf(':');
        const prefix = str.slice(0,i);
        let url = str.slice(i+2);
        if (prefix === 'redirect') {
          if (!url.includes('http')) {
            url = 'http://' + url;
          }
          const urlObj = new URL(url);
          console.log(urlObj);
          window.location.href = urlObj.protocol + '//' + urlObj.host + urlObj.pathname;
        } else {
          setError('shortUrl does not exist');
        }
      } catch (e) {
        console.log(e);
      }
    }
  }, [location.pathname]);

  return (
    <>
      <h1 className="headerText">{message}</h1>
      <div>{error}</div>
    </>
  );
}

export default UserTracking;
