document.getElementById('loginForm').addEventListener('submit', async (event) => {
    event.preventDefault();

    const userid = document.getElementById('userid').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch('/member/sign-in', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ userid, password }),
        });

        if (response.ok) {
            const data = await response.json();
            // 로그인 성공 시 토큰 저장
            localStorage.setItem('accessToken', data.accessToken);
            localStorage.setItem('refreshToken', data.refreshToken);
            localStorage.setItem('memberId', data.memberId);

            const memberId = data.memberId;
            window.location.href = `/content/memberArticles/${memberId}`;
        } else {
            const error = await response.json();
            alert("아이디, 비밀번호를 확인해 주세요.");
        }
    } catch (error) {
        console.error('Login request failed', error);
        alert('Login failed: An unexpected error occurred');
    }
});