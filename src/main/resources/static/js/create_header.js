function createAuthHeaderFromLocalStorage() {
    const authToken = localStorage.getItem('Coding-Mate-Auth');
    const headers = {
        'Content-Type': 'application/json',
    };

    if (authToken) {
        headers['Coding-Mate-Auth'] = `${authToken}`;
    }

    return headers
}