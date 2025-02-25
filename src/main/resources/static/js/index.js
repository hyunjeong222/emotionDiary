document.addEventListener('DOMContentLoaded', () => {
    const memberId = localStorage.getItem('memberId');
    let selectedMonth = document.getElementById('selectedMonth');

    // 현재 선택된 년/월을 저장 (기본 값: 현재 달)
    let currentYearMonth = new Date().toISOString().slice(0, 7);

    // 초기화
    updateMonthDisplay(currentYearMonth);

    // 이전 달 버튼 클릭 시
    document.getElementById('prevButton').addEventListener('click', function() {
        currentYearMonth = adjustMonth(currentYearMonth, -1); // 이전 달로 이동
        loadArticlesByMonth(currentYearMonth, memberId);
    });

    // 다음 달 버튼 클릭 시
    document.getElementById('nextButton').addEventListener('click', function() {
        currentYearMonth = adjustMonth(currentYearMonth, 1); // 다음 달로 이동
        loadArticlesByMonth(currentYearMonth, memberId);
    });

    // 해당 년/월의 게시글을 로드
    function loadArticlesByMonth(yearMonth, memberId) {
        updateMonthDisplay(yearMonth);

        $.ajax({
            url: `/content/memberArticles/${memberId}?yearMonth=${yearMonth}`,
            type: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            },
            success: function(response) {
                const articles = response.articles;
                const articleCount = response.articleCount;

                renderArticles(articles, articleCount);
            },
            error: function(xhr, status, error) {
                console.error('Error:', status, error);
                alert('게시글을 불러오는 중 오류가 발생했습니다.');
            }
        });
    }

    // 감정 이미지 불러오기
    function getEmotionImage(emotionId) {
        let imageSrc;

        switch (emotionId) {
            case 1:
                imageSrc = "data:image/png;base64,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";
                break;
            case 2:
                imageSrc = "data:image/png;base64,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";
                break;
            case 3:
                imageSrc = "data:image/png;base64,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"
                break;
            case 4:
                imageSrc = "data:image/png;base64,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";
                break;
            case 5:
                imageSrc = "data:image/png;base64,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";
                break;
            default:
                break;
        }

        return imageSrc;
    }

    function renderArticles(articles, articleCount) {
        const articleListElement = document.querySelector('.list_wrapper');
        articleListElement.innerHTML = ''; // 기존 게시글 초기화

        // 전체 글 개수
        const headerHTML = `
            <div class="content-header">
                <h2><span>전체 글</span><em>${articleCount}</em></h2>
            </div>
        `;
        articleListElement.innerHTML += headerHTML; // 헤더 추가

        articles.forEach(article => {
            const articleItem = `
                <a class="diaryItem" href="/content/articles/${article.id}">
                    <div class="img_section img_section_${article.emotionId}">
                        <img src="${getEmotionImage(article.emotionId)}" alt="Emotion ${article.emotionId}">
                    </div>
                    <div class="info_section">
                        <div class="created_date">${formatDate(article.createdAt)}</div>
                        <div class="content">${article.content}</div>
                    </div>
                </a>
            `;

            // 게시글 요소 추가
            articleListElement.innerHTML += articleItem;
        });
    }

    // 년/월 표시 업데이트
    function updateMonthDisplay(yearMonth) {
        selectedMonth.textContent = yearMonth;
    }

    // 년/월 조정 (이전 달/다음 달로 이동)
    function adjustMonth(yearMonth, offset) {
        let [year, month] = yearMonth.split('-').map(Number);
        month += offset;
        if (month < 1) {
            month = 12;
            year--;
        } else if (month > 12) {
            month = 1;
            year++;
        }
        return `${year}-${String(month).padStart(2, '0')}`;
    }

    function formatDate(dateString) {
        const date = new Date(dateString);
        const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
        return date.toLocaleDateString('ko-KR', options).replace(/\./g, '.');
    }

    const authButton = document.getElementById('authButton');
    const writeButton = document.getElementById('writeButton');
    const accessToken = localStorage.getItem('accessToken');
    const memberIdInput = document.getElementById('memberId');

    if (memberId) {
        memberIdInput.value = memberId;
    }

    // 로그인 상태에 따라 버튼 설정
    if (accessToken) {
        // 인증된 상태일 때
        authButton.textContent = '로그아웃';
        authButton.addEventListener('click', () => {
            fetch('/member/logout', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${accessToken}`,
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.text();
            })
            .then(message => {
                console.log(message);
                // 로그아웃 처리
                localStorage.removeItem('accessToken');
                localStorage.removeItem('refreshToken');
                localStorage.removeItem('memberId');
                authButton.textContent = '로그인';
                alert('로그아웃 되었습니다.');
                window.location.href = '/';
            })
            .catch(error => {
                console.error('Logout failed', error);
            });
        });
    } else {
        // 인증되지 않은 상태일 때
        authButton.textContent = '로그인';
        authButton.addEventListener('click', () => {
            window.location.href = '/login';
        });
    }

    writeButton.addEventListener('click', () => {
        if (accessToken) {
            fetch('/content/role/newArticle', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${accessToken}`,
                    'Content-Type': 'application/json'
                }
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                console.log('Response data:', data);
                if (data.status === 'redirect') {
                    window.location.href = data.redirectUrl;
                }
            })
            .catch(error => {
                console.error('Request failed', error);
                // 권한이 없거나 오류 발생 시 처리
                alert('서비스를 이용하려면 로그인이 필요합니다.');
                window.location.href = '/login';
            });
        } else {
            console.log('No access token found');
            alert('서비스를 이용하려면 로그인이 필요합니다.');
            window.location.href = '/login';
        }
    });
});