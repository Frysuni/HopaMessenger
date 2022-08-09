const { Client } = require('pg');
const Config = require('./Config.json');
const { Logger } = require('../logger/logger.js');

const client = new Client({ ...Config });

client.connect().then(Logger.Debug('БД подключена успешно.')).catch(e => Logger.Error('Ошибка подключения БД: ' + e));

module.exports = { client };