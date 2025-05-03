const loginButton = document.getElementsByClassName('needAuth')[0];

loginButton.addEventListener('click', async function () {
    const token = localStorage.getItem('coding-mate-auth')

    if (token) {
        const response = await fetch(
            '/api/v1/auth/login', {
                method: 'GET',
                headers: {
                    'coding-mate-auth': `${token}`
                },
            }
        )
        if (response.ok) {
            console.log(`token login ok : ${token}`);
        } else {
            console.log(`token login failed, remove token: ${token}`);
            localStorage.removeItem('coding-mate-auth');
        }
    } else {
        console.log('no token')
        window.location.replace("http://localhost:8080/login");
    }
})