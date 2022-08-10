const bcrypt = require('bcryptjs');
const { client } = require('./databaseConnect.js');

async function authorizateMember(username, password) {
    const result = await client.query(`SELECT password FROM users WHERE username = '${username.toString()}';`);
    if (!result.rows[0]) { return 3511; }
    else if (!password) { return 3512; }
    else if (!bcrypt.compareSync(password.toString(), result.rows[0].password)) { return 3512; }
    else if (bcrypt.compareSync(password.toString(), result.rows[0].password)) { return `connected {"username":"${username}","timestamp":"${Date.now()}"}`; }
    else { return 3510; }
}

module.exports = { authorizateMember };