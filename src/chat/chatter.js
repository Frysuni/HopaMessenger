const WebSocket = require('ws');
const { Logger } = require('../logger/logger');

async function Chatter(MsgObject, clients) {
    MsgObject.timestamp = Date.now();
    Logger.Info('SEND TO ALL: ' + JSON.stringify(MsgObject));
    clients.forEach(function each(client) {
        if (client.readyState === WebSocket.OPEN) {
            client.send('message ' + JSON.stringify(MsgObject));
        }
    });
}

module.exports = { Chatter };