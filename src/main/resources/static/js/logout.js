function logout() {
    fetch('/api/v1/auth/logout', { // 서버의 로그아웃 엔드포인트로 요청
        method: 'POST',
        headers: {
            //'Content-Type': 'application/json' // 필요한 경우 Content-Type 설정
            // 필요한 경우 인증 헤더 (Authorization 등)를 포함할 수 있습니다.
        }
        // body는 일반적으로 필요하지 않습니다.
    })
        .then(response => {
            if (response.ok) {
                localStorage.removeItem('Coding-Mate-Auth');
                localStorage.removeItem('Coding-Mate-Auth-Ref');
                alert('로그아웃 되었습니다.');
                window.location.href = '/login'; // 로그인 페이지로 리다이렉트
            } else {
                console.error('로그아웃 실패:', response.status);
                alert('로그아웃에 실패했습니다.');
            }
        })
        .catch(error => {
            console.error('로그아웃 요청 실패:', error);
            alert('로그아웃 요청에 실패했습니다.');
        });
}