function create_header() {
    const authToken = localStorage.getItem('Authorization');
    const headers = {
        'Content-Type': 'application/json',
    };

    if (authToken) {
        headers['Authorization'] = `${authToken}`;
    }

    return headers
}