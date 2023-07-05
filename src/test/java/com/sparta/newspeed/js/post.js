// 서버에서 게시글 목록을 가져와서 동적으로 생성하도록 구현
var postList = [
    { id: 1, title: '게시글 1' },
    { id: 2, title: '게시글 2' },
    { id: 3, title: '게시글 3' }
];

// 게시글 목록을 동적으로 생성
postList.forEach(function(post) {
    var listItem = document.createElement('li');
    var link = document.createElement('a');
    link.href = '/post/' + post.id;
    link.textContent = post.title;
    listItem.appendChild(link);
    postListElement.appendChild(listItem);
});

// 댓글 작성 폼 제출 이벤트 처리
document.getElementById('comment-form').addEventListener('submit', function(event) {
    event.preventDefault(); // 폼 제출 기본 동작 방지

    // 댓글 작성자와 내용 가져오기
    var author = document.getElementById('comment-author').value;
    var content = document.getElementById('comment-content').value;

    // 댓글 작성 요청 보내기
    sendComment(author, content);
});

// 댓글 추가 함수
function addComment(author, content) {
    // 댓글을 표시할 HTML 요소 생성
    var commentElement = document.createElement('div');
    commentElement.className = 'comment';

    // 작성자와 내용을 표시할 요소 생성
    var authorElement = document.createElement('span');
    authorElement.textContent = '작성자: ' + author;
    var contentElement = document.createElement('p');
    contentElement.textContent = content;

    // 수정 버튼 생성
    var editButton = document.createElement('button');
    editButton.textContent = '수정';
    editButton.className = 'edit-button';

    // 삭제 버튼 생성
    var deleteButton = document.createElement('button');
    deleteButton.textContent = '삭제';
    deleteButton.className = 'delete-button';

    // 수정 버튼 클릭 이벤트 처리
    editButton.addEventListener('click', function() {
        // 수정 기능 구현
        console.log('댓글 수정:', author, content);
    });

    // 삭제 버튼 클릭 이벤트 처리
    deleteButton.addEventListener('click', function() {
        // 삭제 기능 구현
        console.log('댓글 삭제:', author, content);
        // 댓글 요소 제거
        commentElement.remove();
    });

    // 요소들을 댓글 요소에 추가
    commentElement.appendChild(authorElement);
    commentElement.appendChild(contentElement);
    commentElement.appendChild(editButton);
    commentElement.appendChild(deleteButton);

    // 댓글을 표시할 영역 선택 및 추가
    var commentArea = document.getElementById('comment-area');
    commentArea.appendChild(commentElement);
}

// 댓글 작성 요청 보내기 함수
function sendComment(author, content) {
    // AJAX 요청을 사용하여 서버로 댓글 데이터를 전송
    // 이 예시에서는 POST 방식으로 요청을 보내고, JSON 형식으로 데이터를 전송
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/comment', true);
    xhr.setRequestHeader('Content-Type', 'application/json');

    // 요청에 전송할 데이터 객체 생성
    var data = {
        author: author,
        content: content
    };

    // 요청 완료 후의 처리 로직 설정
    xhr.onload = function() {
        if (xhr.status === 200) {
            // 요청이 성공한 경우
            console.log('댓글 작성 성공');
            // 댓글을 화면에 추가하는 함수 호출
            addComment(author, content);
            // 팝업 메시지 출력
            alert('댓글 작성이 완료되었습니다.');
            // 페이지 새로고침
            location.reload();
        } else {
            // 요청이 실패한 경우
            console.log('댓글 작성 실패');
        }
    };

    // 요청 전송
    xhr.send(JSON.stringify(data));
}
