const WebSocket = require('ws');

async function Chatter(MsgObject, clients) {
    MsgObject.timestamp = Date.now();
    clients.forEach(function each(client) {
        if (client.readyState === WebSocket.OPEN) {
            client.send('message ' + JSON.stringify(MsgObject));
        }
    });
}

module.exports = { Chatter };