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
        const [prefix, url] = res.data.split(":");
        if (prefix === 'redirect') {
          console.log(prefix, ' to: ', url);
          window.location.href = 'http://' + url;
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
