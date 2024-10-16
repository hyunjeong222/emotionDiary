let isUserIdAvailable = false;

function checkUserId() {
    const userId = document.getElementById('userId').value;

    if (!userId) {
        alert('아이디를 입력해주세요.');
        document.querySelector('[name="userId"]').focus();
        return;
    }
    const usernamePattern = /^[a-zA-Z0-9]+$/; // 영문, 숫자만 허용
    if (!usernamePattern.test(userId)) {
        alert('아이디는 영문자와 숫자만 사용할 수 있습니다.');
        document.querySelector('[name="userId"]').focus();
        return;
   }

    fetch(`/member/check-userid?userId=${encodeURIComponent(userId)}`)
        .then(response => response.json())
        .then(data => {
            if (data) {
                alert('이미 사용중인 아이디입니다.');
                document.querySelector('[name="userId"]').focus();
                isUserIdAvailable = false;
            } else {
                alert('사용 가능한 아이디입니다.');
                document.querySelector('[name="password"]').focus();
                isUserIdAvailable = true;
            }
        })
        .catch((error) => {
            console.error('Error:', error);
            alert('아이디 중복 체크 중 오류가 발생했습니다.');
        });
}

function submitForm() {
    const form = document.getElementById('joinForm');
    const formData = new FormData(form);

    const userId = formData.get('userId');
    const password = formData.get('password');
    const passwordCheck = formData.get('passwordCheck');
    const userName = formData.get('userName');
    const userNickname = formData.get('userNickname');
    const personal = formData.get('personal');

    // 빈칸 검사 및 포커스 맞추기
    if (!userId) {
        alert('아이디를 입력해주세요.');
        document.querySelector('[name="userId"]').focus();
        return;
    }
    const usernamePattern = /^[a-zA-Z0-9]+$/; // 영문, 숫자만 허용
    if (!usernamePattern.test(userId)) {
        alert('아이디는 영문자와 숫자만 사용할 수 있습니다.');
        document.querySelector('[name="userId"]').focus();
        return;
    }
    if (!isUserIdAvailable) {
        alert('아이디 중복 체크를 해주세요.');
        return;
    }
    if (!password) {
        alert('비밀번호를 입력해주세요.');
        document.querySelector('[name="password"]').focus();
        return;
    }
    // 비밀번호 정규식 검사
    const passwordPattern = /(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\W)(?=\S+$).{8,20}/;
    if (!passwordPattern.test(password)) {
        alert('비밀번호는 8-20자 사이여야 하며, 숫자, 문자, 특수문자를 포함해야 합니다.');
        document.querySelector('[name="password"]').focus();
        return;
    }
    if (!passwordCheck) {
        alert('비밀번호 확인을 입력해주세요.');
        document.querySelector('[name="passwordCheck"]').focus();
        return;
    }
    // 비밀번호 확인
    if (password !== passwordCheck) {
        alert('비밀번호가 일치하지 않습니다.');
        document.querySelector('[name="passwordCheck"]').focus();
        return;
    }
    if (!userName) {
        alert('이름을 입력해주세요.');
        document.querySelector('[name="userName"]').focus();
        return;
    }
    if (!userNickname) {
        alert('닉네임을 입력해주세요.');
        document.querySelector('[name="userNickname"]').focus();
        return;
    }
    // 개인정보 동의 체크 확인
    if (!personal) {
        alert('개인정보 수집 및 이용약관에 동의해주세요.');
        return;
    }

    // 요청할 데이터 준비
    const requestData = {
        userid: userId,
        password: password,
        name: userName,
        nickname: userNickname,
        personal: personal === 'on' // 체크박스가 체크된 경우 'on' 반환
    };

    // 서버로 요청 전송
    fetch('/member/sign-up', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestData),
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('회원가입이 완료되었습니다.');
            window.location.href = '/login';
        } else {
            alert(`${data.message}`);
        }
    })
    .catch((error) => {
        console.error('Error:', error);
        alert('회원가입 중 오류가 발생했습니다.');
    });
}