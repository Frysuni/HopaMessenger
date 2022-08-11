const WebSocket = require('ws');
const { Logger } = require('../logger/logger.js');

async function declareConnect(WebSocketSrv, WSConnection, authResponce) {
    Logger.Info(`Успешная авторизация. User: ${WSConnection.username}, Pass: ${WSConnection.password}, IP: ${WSConnection.ip}`);
    WSConnection.isAuthorized = true;
    const authedmembers = new Array();

    WebSocketSrv.clients.forEach((client) => {
        if (client.readyState === WebSocket.OPEN) {
            client.send(authResponce);
            if (client.username != WSConnection.username && client.isAuthorized) {
                authedmembers.push(client.username);
            }
        }
    });

    const map = { authedmembers };
    WSConnection.send('map ' + JSON.stringify(map));

}

async function declareDisconnect(WebSocketSrv, WSConnection) {
    Logger.Info(`Отключен. User: ${WSConnection.username}, Pass: ${WSConnection.password}, IP: ${WSConnection.ip}`);
    WebSocketSrv.clients.forEach((client) => {
        if (client.readyState === WebSocket.OPEN) {
            client.send(`disconnect {"username":"${WSConnection.username}","timestamp":${Date.now()}}`);
        }
    });
}

module.exports = { declareConnect, declareDisconnect };