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
      `;
    }
});