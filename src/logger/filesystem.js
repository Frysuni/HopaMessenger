const fs = require('fs');

const DayDate = new Intl.DateTimeFormat('en-EU', { day: 'numeric', month: 'long', year: 'numeric' })
    .format(new Date())
    .replace(', ', '-')
    .replace(' ', '-');

async function verifyFile() {
    if (!fs.existsSync('./logs/')) {
        fs.mkdirSync('./logs');
    }
    if (!fs.existsSync(`./logs/${DayDate}.log`)) {
        fs.writeFileSync(`./logs/${DayDate}.log`, DayDate + ' Log File\n');
    }
}

async function writeToFile(data) {
    await verifyFile();
    fs.appendFileSync(`./logs/${DayDate}.log`, data + '\n');
}

module.exports = { writeToFile };