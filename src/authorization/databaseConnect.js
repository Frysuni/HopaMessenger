const { Client } = require('pg');
const Config = require('./Config.json');

const client = new Client({ ...Config });

client.connect();

module.exports = { client };