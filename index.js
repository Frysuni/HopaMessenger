/* eslint-disable no-constant-condition */
const { Logger } = require('./src/logger/logger.js');

async function start() {
    try {
        require('./src/websocket/handler.js');
        Logger.Info('Сервер запущен.');
    } catch (e) {
        Logger.Error('Краш сервера: ' + e);
        start();
    }
}

start();