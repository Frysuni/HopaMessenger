const { WebSocketSrv } = require('./server.js');
const { MessageHandler } = require('../MessageHandler.js');
const { Logger } = require('../logger/logger.js');

WebSocketSrv.on('connection', (WSConnection) => {
    Logger.Info('Новое подключение.');
    WSConnection.on('message', async (msg) => {
        Logger.Debug('RECIEVE: ' + msg);
        WSConnection.send(await MessageHandler(`${msg}`, WebSocketSrv.clients));
    });
});
