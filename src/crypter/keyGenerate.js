const fs = require('fs');

const HexSym = ['A', 'B', 'C', 'D', 'E', 'F', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'];

async function takeRandomSymbol() {
    const RandomInt = Math.floor(Math.random() * 15);
    return HexSym[RandomInt];
}

async function keyGenerate() {
    let Key = '';
    for (let i = 0; i != 10; i++) {
        Key += await takeRandomSymbol();
    }
    return Key;
}

async function getKey() {
    if (!fs.existsSync('./src/crypter/crypter.key')) {
        const KeyObj = {
            hour: new Date().getHours(),
            key: await keyGenerate()
        };
        fs.writeFileSync('./src/crypter/crypter.key', JSON.stringify(KeyObj));
        return KeyObj.key;
    }
    else if (JSON.parse(fs.readFileSync('./src/crypter/crypter.key')).day != new Date().getDate()) {
        const KeyObj = {
            hour: new Date().getHours(),
            key: await keyGenerate()
        };
        fs.writeFileSync('./src/crypter/crypter.key', JSON.stringify(KeyObj));
        return KeyObj.key;
    }
    else if (JSON.parse(fs.readFileSync('./src/crypter/crypter.key')).day == new Date().getDate()) {
        return JSON.parse(fs.readFileSync('./src/crypter/crypter.key')).key;
    }
    else { return 'error'; }
}

module.exports = { getKey };