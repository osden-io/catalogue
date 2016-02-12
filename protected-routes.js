var express = require('express'),
    jwt     = require('express-jwt'),
    config  = require('./config'),
    fileLocator  = require('./fileLocator');

var app = module.exports = express.Router();

var jwtCheck = jwt({
  secret: config.secret
});

app.use('/api/protected', jwtCheck);

app.get('/api/protected/random-file', function(req, res) {
  res.status(200).send(fileLocator.getRandomOne());
});
