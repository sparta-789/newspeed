document.getElementById("login-form").addEventListener("submit", function(event) {
    event.preventDefault(); // 기본 제출 동작 막기

    var username = document.getElementById("username-input").value; // 사용자명 입력 필드의 값 가져오기
    var password = document.getElementById("password-input").value; // 비밀번호 입력 필드의 값 가져오기

    // 사용자명과 비밀번호를 요청 본문에 포함시켜 POST 요청 보내기
    fetch('/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: username,
            password: password
        })
    })
        .then(response => {
            if (response.ok) {
                // 로그인 성공 시 리디렉션
                window.location.href = "../html/index.html";
            } else {
                // 로그인 실패 시 에러 메시지 처리 등을 수행
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
});
