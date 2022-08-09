/* eslint-disable no-constant-condition */
const { Logger } = require('./src/logger/logger.js');

require('./src/websocket/handler.js');
Logger.Info('Сервер запущен.');