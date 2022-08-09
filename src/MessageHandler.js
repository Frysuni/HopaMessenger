const { Chatter } = require('./chat/chatter.js');
const { authorizateMember } = require('./authorization/authorizateMember.js');
const WebSocket = require('ws');
const { getKey } = require('./crypter/keyGenerate.js');
const { Logger } = require('./logger/logger.js');

const AuthedMembers = {};

async function MessageHandler(message, clients) {
    const key = await getKey(clients);
    if (message.startsWith('message ')) {
        const AuthRecive = JSON.parse(message.slice(8));
        if (AuthedMembers[AuthRecive.author] != 'auth') {
            Logger.Info('Попытка неавторизованной отправки сообщения.');
            return '{"Пошел нахуй":"Вы не авторизованы. Кого ты решил взломать?"}';
        }
        Chatter(AuthRecive, clients);
    }
    else if (message.startsWith('auth ')) {
        const AuthResponce = await authorizateMember(JSON.parse(message.slice(5)));

        if (AuthResponce.status == true) {
            AuthedMembers[AuthResponce.username] = 'auth';
            const map = {
                key,
                authedmembers: Object.keys(AuthedMembers)
            };
            Logger.Info(AuthResponce.username + ' авторизован успешно.');
            Logger.Debug(`SEND TO CLIENT: map ${JSON.stringify(map)}`);
            return `map ${JSON.stringify(map)}`;
        }
        Logger.Debug('SEND TO ALL: ' + JSON.stringify(AuthResponce));
        clients.forEach(function each(client) {
            if (client.readyState === WebSocket.OPEN) {
                client.send('auth ' + JSON.stringify(AuthResponce));
            }
        });
    }
    else if (message.startsWith('disconnect ')) {
        const AuthResponce = await authorizateMember(JSON.parse(message.slice(11)));

        if (AuthResponce.status == true) {
            delete AuthedMembers[AuthResponce.username];
            Logger.Info(AuthResponce.username + ' отключился.');
        }

        delete AuthResponce.reason;
        Logger.Debug('SEND TO ALL: ' + JSON.stringify(AuthResponce));
        clients.forEach(function each(client) {
            if (client.readyState === WebSocket.OPEN) {
                client.send('disconnect ' + JSON.stringify(AuthResponce));
            }
        });
    }
    else { Logger.Error('undefined prefix'); }
}

module.exports = { MessageHandler };