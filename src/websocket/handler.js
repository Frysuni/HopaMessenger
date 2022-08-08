const { WebSocketSrv } = require('./server.js');
const { MessageHandler } = require('../MessageHandler.js');

WebSocketSrv.on('connection', (WSConnection) => {
    WSConnection.on('message', async (msg) => {
        console.log(`${msg}`);
        WSConnection.send(await MessageHandler(`${msg}`, WebSocketSrv.clients));
    });
});
