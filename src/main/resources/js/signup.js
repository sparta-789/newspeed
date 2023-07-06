document.getElementById('signup-form').addEventListener('submit', function(event) {
    event.preventDefault(); // 폼 제출 기본 동작 방지

    // 폼 입력값 가져오기
    var username = document.getElementById('username').value;
    var password = document.getElementById('password').value;
    var email = document.getElementById('email').value;
    var userid = document.getElementById('userid').value;

    // 회원 정보 객체 생성
    var user = {
        username : username,
        password : password,
        email : email,
        userid : userid
    }

    var message = '회원가입이 완료되었습니다!';
    alert(message);

    // 회원 정보를 서버에 전송하여 DB에 저장
    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'api/auth/signup', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onreadystatechange = function() {
        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            // 요청이 성공적으로 처리되었을 때의 동작
            var response = JSON.parse(xhr.responseText);
            alert(response.message);
            window.location.href = '/index.html'; // 메인 페이지 URL로 변경해야 함
            // 회원가입 완료 후 다른 페이지로 이동 가능
        } else if (xhr.readyState === XMLHttpRequest.DONE) {
            // 요청이 실패하거나 오류가 발생했을 때의 동작
            alert('회원가입에 실패했습니다.');
        }
    };
    xhr.send(JSON.stringify(user));
});

