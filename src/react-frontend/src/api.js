// Read the host address and the port from the environment
const hostname = process.env.HOST || 'localhost';
const port = process.env.PORT || '8080';

export const REST_API_URL = `http://${hostname}:${port}`;
