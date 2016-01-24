var express = require('express');  
var request = require('request');

var app = express();  
app.use('/', function(req, res) {  
  var url = req.url.replace('/?url=','');
  req.pipe(request(url)).pipe(res);
});

app.listen(process.env.PORT || 3000);
console.log('Server running at localhost:3000/');

