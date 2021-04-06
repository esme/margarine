window.addEventListener('load', (event) => {
  userTracking();
});

async function getIP(){
  const response = await fetch('https://api.ipify.org?format=json');
  const data = await response.json();
  return data.ip;
}

async function sendData(latitude, longitude, timestamp) {
  const data = {latitude, longitude, timeClicked};
  console.log(1, data);

  // replace link with backend server API endpoint
  fetch(`http://localhost:8080/click/${window.location.pathname}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
  })
  .then(response => response.json())
  // redirect to long url
  // .then(data => {
  //   window.location.href = data.longUrl;
  // });
}

function userTracking() {
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
