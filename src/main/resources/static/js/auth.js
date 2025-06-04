// check_and_refresh_token_for_nav_bar 함수는 비동기적으로 토큰을 확인하고 갱신합니다.
async function check_and_refresh_token_for_nav_bar() {
    console.log('checkAndRefreshAccessTokenForNavbar');
    const accessToken = localStorage.getItem('Authorization'); // 액세스 토큰은 로컬 스토리지에서 가져옵니다.

    const navElement = document.getElementById('mainNav');
    const navLeft = navElement.querySelector('.nav-left');
    const navRight = navElement.querySelector('.nav-right');

    if (accessToken) {
        // 1. Access Token 유효성 검증 시도 (GET /api/v1/auth/tokens)
        try {
            const validateResponse = await fetch('/api/v1/auth/tokens', {
                method: 'GET',
                headers: {
                    'Authorization': accessToken
                }
            });

            if (validateResponse.ok) {
                // Access Token이 유효하면 인증된 사용자 내비게이션 바를 업데이트합니다.
                console.log('Access Token is valid.');
                update_navbar_authenticated(navLeft, navRight);
                return true;
            } else if (validateResponse.status === 401) {

                const refreshResult = await refresh_access_token(); // 갱신 API 호출
                if (refreshResult) {
                    // 갱신 성공 시 인증된 사용자 내비게이션 바를 업데이트합니다.
                    console.log('Token refresh successful.');
                    update_navbar_authenticated(navLeft, navRight);
                    return true;
                } else {
                    // 갱신 실패 (Refresh Token도 만료되었거나 유효하지 않음)
                    console.log('Token refresh failed, re-login required.');
                    localStorage.removeItem('Authorization'); // 액세스 토큰 제거
                    update_navbar_guest(navLeft, navRight); // 비로그인 내비게이션 바 표시
                    return false;
                }
            }

        } catch (error) {
            console.error('Error during Access Token validation:', error);
            localStorage.removeItem('Authorization'); // 오류 발생 시 토큰 제거
            update_navbar_guest(navLeft, navRight); // 비로그인 내비게이션 바 표시
            return false;
        }
    }
    // 액세스 토큰이 없으면 비로그인 내비게이션 바를 표시합니다.
    console.log('No Access Token found, displaying guest navbar.');
    update_navbar_guest(navLeft, navRight);
    return false;
}

// refresh_access_token 함수는 리프레시 토큰을 사용하여 액세스 토큰을 갱신합니다.
async function refresh_access_token() {
    console.log('Attempting to refresh access token...');
    try {
        // 1. 토큰 재발급 API 호출 (POST /api/v1/auth/tokens)
        // Refresh Token은 HttpOnly 쿠키로 설정되어 있으므로, 브라우저가 자동으로 요청에 포함시킵니다.
        const refreshResponse = await fetch('/api/v1/auth/tokens', {
            method: 'POST'
        });

        if (refreshResponse.ok) {
            // 재발급 성공: 서버의 ACCESS_TOKEN_HEADER_NAME에 맞춰 새로운 Access Token을 응답 헤더에서 가져옵니다.
            // 백엔드에서 @Value("${jwt.header}")가 "Coding-Mate-Auth"로 설정되어 있다고 가정합니다.
            const newAccessToken = refreshResponse.headers.get('Authorization');

            // 새로운 Refresh Token은 Set-Cookie 헤더를 통해 브라우저가 자동으로 갱신하므로,
            // JavaScript에서 추가적으로 처리할 필요가 없습니다.

            if (newAccessToken) {
                // 새로운 Access Token을 로컬 스토리지에 저장합니다.
                localStorage.setItem('Authorization', newAccessToken);
                console.log('Access Token refreshed and saved successfully.');
                return true;
            } else {
                // 서버가 200 OK를 반환했지만 새로운 Access Token 헤더가 없는 경우
                console.error('New Access Token header (Coding-Mate-Auth) not found in response after refresh.');
                return false;
            }
        } else {
            // 재발급 요청 실패 (예: Refresh Token이 유효하지 않거나 만료됨)
            const errorBody = await refreshResponse.json();
            console.error('Refresh Token request failed:', refreshResponse.status, errorBody.message || refreshResponse.statusText);
            // 서버에서 401 응답과 함께 Set-Cookie: Max-Age=0; 등으로 리프레시 토큰 쿠키 삭제를 지시했을 것입니다.
            return false;
        }
    } catch (error) {
        // 네트워크 오류 등 예외 발생
        console.error('Error during Refresh Token request:', error);
        return false;
    }
}

// logout 함수는 서버에 로그아웃 요청을 보내고 클라이언트 측 토큰을 제거합니다.
async function logout() {
    console.log('Attempting to log out...');
    try {
        const response = await fetch('/api/v1/auth/sign-out', {
            method: 'DELETE',
        });

        if (response.ok) {
            localStorage.removeItem('Authorization'); // Access Token 제거
            // Refresh Token 쿠키는 서버의 `Set-Cookie: ... Max-Age=0` 응답으로 브라우저에서 자동으로 삭제됩니다.
            alert('로그아웃 되었습니다.');
            window.location.href = '/login'; // 로그인 페이지로 리다이렉트
        } else {
            console.error('로그아웃 실패:', response.status);
            alert('로그아웃에 실패했습니다.');
        }
    } catch (error) {
        console.error('로그아웃 요청 중 오류:', error);
        alert('로그아웃 요청에 실패했습니다.');
    }
}

// update_navbar_authenticated 함수는 로그인된 사용자에게 맞는 내비게이션 바를 표시합니다.
function update_navbar_authenticated(navLeft, navRight) {
    navLeft.innerHTML = `
    <a href="/">홈</a>
    <a href="/answer/list">풀이 목록</a>
`;
    navRight.innerHTML = `
    <a href="/my-page">프로필</a>
    <a href="#" id="logoutButton">로그아웃</a>
`;

    const logoutButton = document.getElementById('logoutButton');
    if (logoutButton) {
        logoutButton.addEventListener('click', function (event) {
            event.preventDefault(); // <a> 태그의 기본 동작(페이지 이동) 방지
            logout(); // 로그아웃 함수 호출
        });
    }
}

// update_navbar_guest 함수는 로그인되지 않은 사용자에게 맞는 내비게이션 바를 표시합니다.
function update_navbar_guest(navLeft, navRight) {
    navLeft.innerHTML = `
    <a href="/">홈</a>
`;
    navRight.innerHTML = `
    <a href="/login">로그인</a>
    <a href="/register">회원가입</a>
`;
}