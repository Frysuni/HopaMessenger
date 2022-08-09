const WebSocketServer = require('ws');
const { Logger } = require('../logger/logger');

const WebSocketSrv = new WebSocketServer.Server({
    port: 8553,
    perMessageDeflate: {
      zlibDeflateOptions: {
        chunkSize: 1024,
        memLevel: 7,
        level: 3
      },
      zlibInflateOptions: {
        chunkSize: 10 * 1024
      },
      clientNoContextTakeover: true,
      serverNoContextTakeover: true,
      serverMaxWindowBits: 10,
      concurrencyLimit: 10,
      threshold: 1024
    }
});
Logger.Debug('WebSocket is started');
module.exports = { WebSocketSrv };