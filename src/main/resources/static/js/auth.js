// check_and_refresh_token_for_nav_bar 함수는 비동기적으로 토큰을 확인하고 갱신합니다.
async function check_and_refresh_token_for_nav_bar() {
    console.log('checkAndRefreshAccessTokenForNavbar');
    const accessToken = localStorage.getItem('Coding-Mate-Auth');
    const refreshToken = localStorage.getItem('Coding-Mate-Auth-Ref');
    const navElement = document.getElementById('mainNav');
    const navLeft = navElement.querySelector('.nav-left');
    const navRight = navElement.querySelector('.nav-right');

    if (accessToken) {
        // 액세스 토큰으로 유효성을 확인합니다.
        const response = await fetch('/api/v1/auth/tokens', {
            method: 'GET',
            headers: {
                'Coding-Mate-Auth': accessToken
            }
        });

        if (response.ok) {
            // 토큰이 유효하면 인증된 사용자 내비게이션 바를 업데이트합니다.
            update_navbar_authenticated(navLeft, navRight);
            return true;
        } else if (response.status === 401 && refreshToken) {
            // 액세스 토큰이 만료되었지만 리프레시 토큰이 있으면 갱신을 시도합니다.
            const refreshResult = await refresh_access_token();
            if (refreshResult) {
                // 갱신 성공 시 인증된 사용자 내비게이션 바를 업데이트합니다.
                update_navbar_authenticated(navLeft, navRight);
                return true;
            }
        }
    }
    // 액세스 토큰이 없거나 갱신에 실패한 경우, 비로그인 내비게이션 바를 표시합니다.
    update_navbar_guest(navLeft, navRight);
    return false;
}

// refresh_access_token 함수는 리프레시 토큰을 사용하여 액세스 토큰을 갱신합니다.
async function refresh_access_token() {
    const refreshToken = localStorage.getItem('Coding-Mate-Auth-Ref');
    if (!refreshToken) {
        console.log('Refresh Token이 없습니다.');
        return false;
    }
    try {
        const refreshResponse = await fetch('/api/v1/auth/tokens', {
            method: 'POST',
            headers: {
                'Coding-Mate-Auth-Ref': `${refreshToken}`
            }
        });

        if (refreshResponse.ok) {
            const newTokenData = await refreshResponse.json();
            // 새로운 토큰 데이터가 유효한지 확인하고 로컬 스토리지에 저장합니다.
            if (newTokenData && newTokenData.data && newTokenData.data.accessToken && newTokenData.data.refreshToken) {
                localStorage.setItem('Coding-Mate-Auth', newTokenData.data.accessToken);
                localStorage.setItem('Coding-Mate-Auth-Ref', newTokenData.data.refreshToken);
                console.log('토큰 갱신 성공:', newTokenData.data);
                return true;
            } else {
                console.error('새로운 토큰 응답에 accessToken 또는 refreshToken 없음:', newTokenData);
                localStorage.removeItem('Coding-Mate-Auth');
                localStorage.removeItem('Coding-Mate-Auth-Ref');
                return false;
            }
        } else {
            console.error('Refresh Token 요청 실패:', refreshResponse.status);
            localStorage.removeItem('Coding-Mate-Auth');
            localStorage.removeItem('Coding-Mate-Auth-Ref');
            return false;
        }
    } catch (error) {
        console.error('Refresh Token 요청 중 오류:', error);
        localStorage.removeItem('Coding-Mate-Auth');
        localStorage.removeItem('Coding-Mate-Auth-Ref');
        return false;
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


// logout 함수는 서버에 로그아웃 요청을 보내고 로컬 스토리지에서 토큰을 제거합니다.
function logout() {
    fetch('/api/v1/auth/sign-out', {
        method: 'DELETE',
        headers: {
            'Coding-Mate-Auth-Ref': localStorage.getItem('Coding-Mate-Auth-Ref')
        }
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

// DOMContentLoaded 이벤트 리스너: HTML 문서가 완전히 로드되고 파싱된 후에 함수를 실행합니다.
document.addEventListener('DOMContentLoaded', () => {
    check_and_refresh_token_for_nav_bar();
});