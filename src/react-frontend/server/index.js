const express = require('express');
const app = express();
const path = require("path");

const PORT = 3000;

app.use(express.static(path.join(__dirname, "..", "build")));
app.use(express.static(path.join(__dirname, "..", "public")));

app.get("*", (req, res) => {
  res.redirect('http://google.com');
});

app.listen(PORT, () => {
  console.log(`Server running at: http://localhost:${PORT}/`);
});
