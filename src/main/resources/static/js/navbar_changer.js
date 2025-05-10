document.addEventListener('DOMContentLoaded', function () {
    const navElement = document.getElementById('mainNav');
    const navLeft = navElement.querySelector('.nav-left');
    const navRight = navElement.querySelector('.nav-right');

    if (localStorage.getItem('Coding-Mate-Auth')) {
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
            logoutButton.addEventListener('click', function(event) {
                event.preventDefault(); // 링크의 기본 동작 막기
                localStorage.removeItem('Coding-Mate-Auth'); // 로컬 스토리지에서 인증 정보 제거
                alert('로그아웃 되었습니다.');
                window.location.href = '/'; // 홈페이지로 리다이렉트
            });
        }
    }
});