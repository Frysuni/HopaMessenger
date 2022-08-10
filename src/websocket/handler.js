const WebSocket = require('ws');
const { WebSocketSrv } = require('./server.js');
const { MessageHandler } = require('../MessageHandler.js');
const { Logger } = require('../logger/logger.js');
const { authorizateMember } = require('../authorization/authorizateMember.js');
const { declareConnect, declareDisconnect } = require('../authorization/declare.js');
/*
async function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

 async function chesckAlive(WSConnection, WSConnectionData) {
    setInterval(() => {
        WSConnection.on('pong', () => this.isAlivee = true);
        WebSocketSrv.clients.forEach(function each(ws) {
            if (ws.isAlivee === false) return ws.terminate();

            ws.isAlivee = false;
            ws.ping();
          });
    }, 10000);
}*/

WebSocketSrv.on('connection', async (WSConnection, WSConnectionData) => {
    WSConnection.ip = WSConnectionData.socket.remoteAddress.replace('::ffff:', '');
    WSConnection.username = WSConnectionData.headers.username;
    WSConnection.password = WSConnectionData.headers.password;
    WSConnection.isAuthorized = false;

    if (WSConnection.username === undefined || WSConnection.password === undefined) {
        WSConnection.close(3500);
        Logger.Error('Попытка подключения без username/password headers. IP: ' + WSConnection.ip);
        return;
    }

    const authResponce = await authorizateMember(WSConnection.username, WSConnection.password);
    if (typeof authResponce == 'number') {
        WSConnection.close(authResponce);
        Logger.Debug(`Неудачная авторизация. User: ${WSConnection.username}, Pass: ${WSConnection.password}, IP: ${WSConnection.ip}`);
        return;
    }

    else {
        let stop = false;
        WebSocketSrv.clients.forEach((client) => {
            if (client.readyState === WebSocket.OPEN) {
                if (client.username == WSConnection.username && client.isAuthorized) {
                    WSConnection.close(3513);
                    Logger.Debug(`Дублирование подключения. User: ${WSConnection.username}, Pass: ${WSConnection.password}, IP: ${WSConnection.ip}`);
                    stop = true;
                }
            }
        });
        if (!stop) declareConnect(WebSocketSrv, WSConnection, authResponce);
        else return;
    }

    WSConnection.on('close', (code) => {
        if (code == 3500 || code == 3510 || code == 3511 || code == 3512 || code == 3513) return;
        declareDisconnect(WebSocketSrv, WSConnection);
    });
    /* WSConnection.isAlivee = true;
    console.log(WSConnection.isAlivee)
    WSConnection.on('pong', () => {
        WSConnection.isAlivee = true;
        console.log('huy')
    });*/

    WSConnection.on('message', async (msg) => {
        Logger.Debug('RECIEVE: ' + msg);
        WSConnection.send(await MessageHandler(`${msg}`, WebSocketSrv.clients));
    });
});
/* WebSocketSrv.on('headers', (headers, req) => {
    req.huy = 'huy';
    // console.log(headers, req);
});*/
/* const interval = setInterval(() => {
    WebSocketSrv.clients.forEach((WSConnection) => {
        if (WSConnection.isAlivee == false) {
            console.log(WSConnection.isAlivee);
            Logger.Error('Потеряно соединение с ' + WSConnection.ip);
            return WSConnection.terminate();
        }
        WSConnection.isAlivee = false;
        WSConnection.ping();
    });
}, 1000); */
