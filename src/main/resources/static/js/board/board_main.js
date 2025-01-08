//페이지네이션을 위함
window.onload = () => {
    setQueryStringParams();
    updateSelectedButton(); // 추가

}

//쿼리 스트링 파라미터 세팅
function setQueryStringParams() {
    // 현재 URL의 쿼리 스트링 파라미터 추출
    const params = new URLSearchParams(location.search);

    // 검색 폼 가져오기
    const form = document.getElementById('searchForm');

    // 쿼리 스트링이 없으면 종료
    //  if (!location.search || !form) {
    //      return;
    //  }

    // 쿼리 스트링 값을 폼 요소에 매핑
    params.forEach((value, key) => {
        if (form[key]) {
            form[key].value = value; // 해당 키와 일치하는 폼 요소에 값 설정
        }
    });
}


function updateSelectedButton() {
    const path = location.pathname; // 현재 경로 추출
    const buttons = document.querySelectorAll('.mail_btn input'); // 모든 버튼 선택

    buttons.forEach((btn) => {
        const url = btn.getAttribute('onclick').match(/'(.*?)'/)[1]; // onclick의 URL 추출
        if (url === path) {
            btn.classList.add('selected'); // 현재 경로와 URL이 일치하면 selected 추가
        } else {
            btn.classList.remove('selected'); // 일치하지 않으면 selected 제거
        }
    });
}

function selectButton(button, url) {
    location.href = url; // 페이지 이동
}