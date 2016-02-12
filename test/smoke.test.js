'use strict';

var test = require('tape');
var request = require('supertest');
var server = require('../server');

test('smoke test', function(t) {
	request(server)
	.get('/api/random-file')
	.expect(200)
	.expect('Content-Type', /text\/html; charset=utf-8/)
	.end(function(err, res) {
		t.error(err);
		t.end();
		server.close();
	});	
});