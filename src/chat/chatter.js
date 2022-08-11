const WebSocket = require('ws');
const { Logger } = require('../logger/logger');

async function Chatter(message, clients) {
    if (message.startsWith('message ')) {
        const MsgObject = JSON.parse(message.slice(8));
        MsgObject.timestamp = Date.now();
        Logger.Info('SEND TO ALL: messsage ' + JSON.stringify(MsgObject));
        clients.forEach((client) => {
            if (client.readyState === WebSocket.OPEN) {
                client.send('message ' + JSON.stringify(MsgObject));
            }
        });
    }
}

module.exports = { Chatter };