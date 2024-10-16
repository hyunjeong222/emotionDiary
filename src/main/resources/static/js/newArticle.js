document.addEventListener('DOMContentLoaded', () => {
    const memberIdInput = document.getElementById('memberId');
    const btnInsert = document.getElementById('btn-insert');
    const btnUpdate = document.getElementById('btn-update-insert');

    const memberId = localStorage.getItem('memberId');
    if (memberId) {
        memberIdInput.value = memberId;
    }

    const emotionId = document.getElementById('emotionId_db').value;
    const emotionItems = document.querySelectorAll('.EmotionItem');

    // 해당 emotionId를 가진 요소를 찾아서 선택된 상태로 변경
    emotionItems.forEach(item => {
        if (item.dataset.emotionId === emotionId) {
            item.style.backgroundColor = item.dataset.color; // 배경색을 설정하여 선택된 상태로 표시
        } else {
            item.style.backgroundColor = ''; // 선택되지 않은 요소는 기본 색으로 설정
        }
    });

    // 선택된 상태로 변경 및 input 필드에 emotionId 설정
    emotionItems.forEach(item => {
        item.addEventListener('click', function() {
            // 모든 요소의 배경색을 초기화
            emotionItems.forEach(el => el.style.backgroundColor = '');
            // 클릭한 요소의 배경색을 변경
            this.style.backgroundColor = this.dataset.color;
            // hidden input에 선택된 emotionId 설정
            document.getElementById('emotionId').value = this.dataset.emotionId;
        });
    });

    if (btnInsert) {
        btnInsert.addEventListener('click', function(event) {
            event.preventDefault();

            // 폼 요소 가져오기
            const form = document.getElementById('addArticleForm'); // form 요소를 선택
            const formData = new FormData(form); // form 요소에서 FormData 생성

            // 폼 데이터 수집
            const data = {
                createdAt: formData.get('createdAt'),
                emotionId: formData.get('emotionId'),
                content: formData.get('content'),
                memberId: formData.get('memberId')
            };

            fetch('/content/addArticle', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
            .then(response => response.json())
            .then(result => {
                // 성공 시 처리
                alert('일기가 성공적으로 저장되었습니다!');
                window.location.href = `/content/memberArticles/${memberId}`;
            })
            .catch(error => {
                // 오류 시 처리
                console.error('Error:', error);
                alert('일기 저장 중 오류가 발생했습니다.');
            });
        });
    } else if (btnUpdate) {
        btnUpdate.addEventListener('click', function(event) {
            event.preventDefault();

            const form = document.getElementById('addArticleForm');
            const formData = new FormData(form);

            const data = {
                createdAt: formData.get('createdAt'),
                emotionId: formData.get('emotionId'),
                content: formData.get('content')
            };

            // 쿼리 스트링에서 'id' 값을 꺼내어 사용
            let params = new URLSearchParams(window.location.search);
            let id = params.get('id');

            fetch(`/content/newArticle/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
            .then(response => response.json())
            .then(result => {
                alert('일기가 성공적으로 수정되었습니다!');
                window.location.href = `/content/articles/${id}`;
            })
            .catch(error => {
                console.error('Error:', error);
                alert('일기 수정 중 오류가 발생했습니다.');
            });
        });
    } else {
        console.error('버튼 요소를 찾을 수 없습니다.');
    }

    const cancelButton = document.querySelector('.button_undefined');
    const articleId = document.querySelector('.button_undefined').value;

    cancelButton.addEventListener('click', () => {
        cancelForm(articleId);
    });

    // 삭제 기능
    const deleteButton = document.getElementById('delete-btn');

    if(deleteButton){
        deleteButton.addEventListener('click', e => {
            let params = new URLSearchParams(window.location.search);
            let id = params.get('id');

            fetch(`/content/deleteArticle/${id}`, {
                method: 'DELETE'
            })
            .then(response => {
                if (response.ok) {
                    alert('삭제가 완료되었습니다.');
                    // 로컬 스토리지에서 memberId 가져오기
                    const memberId = localStorage.getItem('memberId');
                    window.location.href = `/content/memberArticles/${memberId}`;
                } else {
                    alert('삭제에 실패했습니다.');
                }
            })
        });
    }

    // 뒤로가기
    const backButton = document.getElementById('button-back');

    if(backButton){
        backButton.addEventListener('click', e => {
            window.history.back();
        });
    }
});


const emotionItems = document.querySelectorAll('.EmotionItem');
// 각 EmotionItem 요소에 클릭 이벤트 리스너 추가
emotionItems.forEach(item => {
    item.addEventListener('click', function() {
        // 모든 EmotionItem 요소의 배경색 초기화
        emotionItems.forEach(emotion => emotion.style.backgroundColor = '');
        // 클릭된 요소의 배경색 변경
        this.style.backgroundColor = this.getAttribute('data-color');
    });
});

// 감정 선택 시 hidden 필드에 값 설정
document.querySelectorAll('.EmotionItem').forEach(item => {
    item.addEventListener('click', function() {
        document.getElementById('emotionId').value = this.getAttribute('data-emotion-id');
        document.querySelectorAll('.EmotionItem').forEach(el => el.classList.remove('selected'));
        this.classList.add('selected');
    });
});

// 취소 버튼 클릭 시 처리
function cancelForm(articleId) {
    document.querySelectorAll('.EmotionItem').forEach(item => {
        // 배경색을 기본으로 설정
        item.style.backgroundColor = '';
        // 선택된 상태 제거
        item.classList.remove('selected');
    });

    // 폼 요소 초기화
    document.getElementById('addArticleForm').reset();
    // 감정 ID 숨겨진 필드 초기화
    document.getElementById('emotionId').value = '';

    // 데이터베이스에 저장된 감정 ID를 가져와서 다시 선택 상태로 설정
    const savedEmotionId = document.getElementById('emotionId_db').value;
    if (savedEmotionId) {
        document.querySelectorAll('.EmotionItem').forEach(item => {
            if (item.dataset.emotionId == savedEmotionId) {
                // 저장된 감정 항목 선택 및 배경색 적용
                item.classList.add('selected');
                item.style.backgroundColor = item.dataset.color;
                document.getElementById('emotionId').value = savedEmotionId;
            }
        });
    }
}