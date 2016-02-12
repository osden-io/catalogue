var files = require('./fileLocator.json');

exports.getRandomOne = function() {
  var totalAmount = files.length;
  var rand = Math.ceil(Math.random() * totalAmount);
  return files[rand];
};
