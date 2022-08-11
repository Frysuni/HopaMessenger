const WebSocket = require('ws');
const { Logger } = require('./logger/logger');

async function MessageHandler(message, clients, WSConnection) {
    if (message.startsWith('message ')) {
        const MsgObject = JSON.parse(message.slice(8));
        MsgObject.timestamp = Date.now();
        MsgObject.author = WSConnection.username;
        clients.forEach((client) => {
            if (client.readyState === WebSocket.OPEN) {
                client.send('message ' + JSON.stringify(MsgObject));
            }
        });
        Logger.Info(`Сообщение от ${WSConnection.username}: ${MsgObject.isEncrypted ? '*Зашифрованное сообщение*' : MsgObject.content.replace('\n', '')}`);
    }
}

module.exports = { MessageHandler };