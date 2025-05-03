document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');

    if (loginForm) {
        loginForm.addEventListener('submit', async (event) => {
            event.preventDefault();  // 폼 제출 기본 동작 방지

            const userId = document.getElementById('userId').value.trim();
            const password = document.getElementById('password').value.trim();

            const token = localStorage.getItem('coding-mate-auth');
            console.log('저장된 토큰:', token);

            if (token) {
                // 토큰으로 로그인 시도
                try {
                    const response = await fetch('/api/users/login/token', {
                        method: 'POST',
                        headers: {
                            'code-for-code-auth': `${token}`
                        }
                    });

                    if (response.ok) {
                        console.log('토큰 로그인 성공');
                        window.location.replace("/"); // 인증된 메인 페이지로 리다이렉트
                    } else {
                        console.log('토큰 로그인 실패');
                        alert('토큰 인증에 실패했습니다. 다시 로그인 해주세요.');
                        localStorage.removeItem('coding-mate-auth'); // 실패 시 토큰 제거
                    }
                } catch (error) {
                    console.error('토큰 로그인 에러:', error);
                    alert('서버 오류가 발생했습니다.');
                }
            } else {
                // 아이디 및 비밀번호로 로그인 시도
                if (userId === '') {
                    alert('아이디를 입력하세요.');
                    return;
                }

                if (password === '') {
                    alert('비밀번호를 입력하세요.');
                    return;
                }

                const data = { userId, password };

                try {
                    const response = await fetch('/api/users/login', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(data)
                    });

                    if (response.ok) {
                        const token = response.headers.get("code-for-code-auth");
                        console.log('서버로부터 받은 토큰:', token);

                        if (token) {
                            localStorage.setItem('coding-mate-auth', token);
                            alert('로그인이 완료되었습니다.');
                            window.location.replace("/"); // 인증된 메인 페이지로 리다이렉트
                        } else {
                            alert('토큰을 받아올 수 없습니다.');
                        }
                    } else {
                        alert('로그인에 실패했습니다. 다시 시도하세요.');
                    }
                } catch (error) {
                    console.error('로그인 에러:', error);
                    alert('서버 오류가 발생했습니다. 나중에 다시 시도해주세요.');
                }
            }
        });
    } else {
        checkLoginStatus();
    }
});

function checkLoginStatus() {
    const token = localStorage.getItem('coding-mate-auth');
    if (!token) {
        alert('로그인이 필요합니다.');
        window.location.replace("/users/login");
    } else {
        console.log('로그인 상태 확인: 토큰이 있습니다.');
    }
}

async function fetchWithToken(url, options = {}) {
    const token = localStorage.getItem('coding-mate-auth');

    if (!token) {
        alert('로그인이 필요합니다.');
        window.location.replace("/users/login");
        return;
    }

    const headers = {
        ...options.headers,
        'code-for-code-auth': `${token}`
    };

    try {
        const response = await fetch(url, {
            ...options,
            headers: headers
        });

        if (response.status === 401) {
            alert('인증 오류가 발생했습니다. 다시 로그인 해주세요.');
            localStorage.removeItem('code-for-code-auth');
            window.location.replace("/users/login");
        }
        return response;
    } catch (error) {
        console.error('에러 발생:', error);
        alert('서버 오류가 발생했습니다.');
    }
}