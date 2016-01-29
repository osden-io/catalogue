'use strict';

var test = require('tape');
var request = require('supertest');
var app = require('../server/server');

//app.use(app.rest());

test('smoke test', function(t) {
	request(app)
	.get('/')
	.expect(200)
	.expect('Content-Type', /json/)
	.end(function(err, res) {
		t.error(err);
		t.end();
	});	
});