const Config = require('./debug.json');
const { writeToFile } = require('./filesystem.js');

const time = new Intl.DateTimeFormat('en-EU', { hour: '2-digit', hourCycle: 'h23', minute: '2-digit', second: '2-digit' }).format(new Date());

async function Info(data) {
    writeToFile(`[${time}][  INFO ] ${data}`);
    console.log(`[${time}][  INFO ] ${data}`);
}

async function Debug(data) {
    if (Config.debug) {
        writeToFile(`[${time}][ DEBUG ] ${data}`);
        console.debug(`[${time}][ DEBUG ] ${data}`);
    }
}

async function Error(data) {
    writeToFile(`[${time}][ ERROR ] ${data}`);
    console.error(`[${time}][ ERROR ] ${data}`);
}

const Logger = {
    Info,
    Debug,
    Error
};

module.exports = { Logger };