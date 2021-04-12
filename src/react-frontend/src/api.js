// Read the host address and the port from the environment
let method = 'http'
let hostname = 'localhost';
let port = '8080';
if (process.env.NODE_ENV === "production") {
  hostname = 'margarine.app';
  port = '8443';
  method = 'https';
}

let location = window.location.origin; // Returns:'https://margarine.app:8443'

if (location.toString().split(":")[2] === "8443") {
  hostname = 'margarine.app';
  port = '8443';
  method = 'https';
}

export const REST_API_URL = `${method}://${hostname}:${port}`;