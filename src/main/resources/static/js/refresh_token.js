function refresh_token() {
    console.log('checkAndRefreshAccessToken')
    const accessToken = localStorage.getItem('Coding-Mate-Auth');
    const refreshToken = localStorage.getItem('Coding-Mate-Auth-Ref');

    if (!accessToken) {
        console.log('Access Token이 없습니다.');
        // 필요하다면 로그인 페이지로 리다이렉트 또는 다른 처리
        return;
    }

    fetch('/api/v1/auth/access-token', {
        method: 'GET',
        headers: {
            'Coding-Mate-Auth': accessToken // 기존 Access Token을 Authorization 헤더에 담아 보냅니다.
        }
    })
        .then(response => {
            if (response.ok) {
                console.log('Access Token이 유효합니다.');
                // Access Token이 유효한 경우 필요한 동작 수행 (예: 다음 페이지 로드)a
                // 이 함수는 토큰 검사만 수행하므로, 실제 동작은 호출하는 곳에서 처리해야 합니다.
                return Promise.resolve(true); // Access Token이 유효함을 반환
            } else if (response.status === 401) {
                console.log('Access Token이 만료되었습니다. Refresh Token으로 갱신을 시도합니다.');
                if (refreshToken) {
                    return fetch('/api/v1/auth/refresh-token', {
                        method: 'GET',
                        headers: {
                            'Coding-Mate-Auth-Ref': `${refreshToken}` // 서버에서 요구하는 헤더명으로 변경
                        }
                    })
                        .then(refreshResponse => {
                            if (refreshResponse.ok) {
                                return refreshResponse.json();
                            } else {
                                console.error('Refresh Token 요청 실패:', refreshResponse.status);
                                localStorage.removeItem('Coding-Mate-Auth');
                                localStorage.removeItem('Coding-Mate-Auth-Ref');
                                // 필요하다면 로그인 페이지로 리다이렉트 또는 사용자에게 알림
                                return Promise.reject(false); // 토큰 갱신 실패
                            }
                        })
                        .then(newTokenData => {
                            console.log(newTokenData)
                            const data = newTokenData.data;
                            if (data.refreshToken && data.accessToken) {
                                localStorage.setItem('Coding-Mate-Auth', `${data.accessToken}`);
                                localStorage.setItem('Coding-Mate-Auth-Ref', `${data.refreshToken}`);
                                console.log('Token 갱신 성공:', data);
                                return Promise.resolve(true); // 토큰 갱신 성공
                            } else {
                                console.error('새로운 Access Token이 응답에 없습니다.');
                                localStorage.removeItem('Coding-Mate-Auth');
                                localStorage.removeItem('Coding-Mate-Auth-Ref');
                                return Promise.reject(false); // 토큰 갱신 실패
                            }
                        })
                        .catch(() => {
                            return Promise.reject(false); // Refresh Token 요청 또는 처리 실패
                        });
                } else {
                    console.log('Refresh Token이 없습니다.');
                    localStorage.removeItem('Coding-Mate-Auth');
                    // 필요하다면 로그인 페이지로 리다이렉트 또는 다른 처리
                    return Promise.reject(false); // Refresh Token 없음
                }
            } else {
                console.error('Access Token 검사 실패:', response.status);
                return Promise.reject(false); // Access Token 검사 실패 (401 외 다른 에러)
            }
        })
        .catch(() => {
            return Promise.reject(false); // 전체적인 오류 발생
        });
}