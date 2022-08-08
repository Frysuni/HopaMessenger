const { client } = require('./databaseConnect.js');


async function compareAuth(result, password) {
    if (!result.rows[0]) { return 'Пользователя с таким именем не существует.'; }
    else if (result.rows[0].password != password) { return 'Неверный пароль.'; }
    else if (result.rows[0].password === password) { return false; }
    else { return 'Неизвестная ошибка. Обратитесь к разработчику. API:authorization/authorizateMember.js:compareAuth'; }
}

async function checkAuth(username, password) {
    try {
        const res = await client.query(`SELECT password FROM users WHERE username = '${username}';`);
        return await compareAuth(res, password);
    } catch (err) {
        console.log(err.stack);
        return 'Неизвестная ошибка. Обратитесь к разработчику. API:authorization/authorizateMember.js:checkAuth';
    }
}

async function authorizateMember(AuthObject) {
    let Auth = {
        username: AuthObject.username,
        status: true,
        reason: null,
        timestamp: Date.now()
    };

    const AuthResponce = await checkAuth(AuthObject.username, AuthObject.password);
    if (AuthResponce) {
        Auth = {
            ...Auth,
            status: false,
            reason: AuthResponce
        };
    }

    return Auth;
}

module.exports = { authorizateMember };