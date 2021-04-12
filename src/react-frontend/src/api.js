// Read the host address and the port from the environment
let hostname = 'localhost';
let port = '8080';
if (process.env.NODE_ENV === "production") {
  hostname = 'margarine.app';
  port = '8443';
}

export const REST_API_URL = `http://${hostname}:${port}`;
