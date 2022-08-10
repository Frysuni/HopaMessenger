const CryptoJS = require('crypto-js');
const Crypter = {
    encrypt: (data, key) => CryptoJS.AES.encrypt(data + Math.floor(Math.random() * 9999), key).toString(),
    decrypt: (data, key) => CryptoJS.AES.decrypt(data, key).toString(CryptoJS.enc.Utf8).slice(0, -4),
};
module.exports = { Crypter };