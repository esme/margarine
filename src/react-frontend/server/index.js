const express = require('express');
const app = express();
const path = require("path");

const PORT = 3000;

app.use(express.json());
app.use(express.static(path.join(__dirname, "..", "build")));
app.use(express.static(path.join(__dirname, "..", "user-tracking")));

app.post('/test', (req, res) => {
  console.log(2, req.body);
  const payload = {'longUrl': 'https://google.com'};
  console.log(3, payload);
  res.send(payload);
});

app.get("/dashboard", (req, res) => {
  res.sendFile(path.join(__dirname, "..", "build", "index.html"));
});

app.get("/:shortUrl", (req, res) => {
  console.log('shortUrl ', req.params.shortUrl);
  res.sendFile(path.join(__dirname, "..", "user-tracking", "index.html"));
});

app.listen(PORT, () => {
  console.log(`Server running at: http://localhost:${PORT}/`);
});
