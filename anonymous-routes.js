var express = require('express'),
    fileLocator  = require('./fileLocator');

var app = module.exports = express.Router();

app.get('/api/random-file', function(req, res) {
  res.status(200).send(fileLocator.getRandomOne());
});
