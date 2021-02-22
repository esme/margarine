window.addEventListener('load', (event) => {
  geoFindMe();
});

async function getIP(){
  const response = await fetch('https://api.ipify.org?format=json');
  const data = await response.json();
  return data.ip;
}

async function sendData(latitude, longitude) {
  const ip = await getIP();
  const data = {ip, 'latitude': latitude, 'longitude': longitude};
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
    const latitude  = position.coords.latitude;
    const longitude = position.coords.longitude;

    status.textContent = '';

    sendData(latitude, longitude);
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
