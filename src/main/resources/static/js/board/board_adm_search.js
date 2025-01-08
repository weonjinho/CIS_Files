const selectcategoryAll = document.querySelector("#selectCategoryO");
const submitButtonOne = document.querySelector("#submitButton");

//카테고리 설렉트박스 선택
function selectCategory() {

    if (selectcategoryAll.value != null) {
        // alert(selectcategoryAll.value);
        //console.log(selectcategoryAll.value);
        submitButtonOne.click();
    }


}
//삭제 function
function deletePost(button) {
    alert("sd");
    // <button th:attr="data-category=${m.category}, data-board-num=${m.board_num}"
    const categoryv = button.getAttribute('data-category')
    const boardNumv = button.getAttribute('data-board-num');
    const fileAttachedv= button.getAttribute('data-file-attached');


    // 디버깅: 콘솔에 데이터 출력
    console.log('카테고리:', categoryv);
    console.log('게시글 번호:', boardNumv);
    console.log('파일첨부여부: ', fileAttachedv);

    if (confirm('정말 삭제하시겠습니까?')) {


        $.ajax({
            url:'/board/manager/delOne/',
            method:'POST',
            data: {
                category:categoryv,
                board_num:boardNumv,
                fileAttached:fileAttachedv
            },
            success:function (response) {
                if(response.status === "success") {
                    console.log("삭제 성공: ",response);
                    alert("삭제되었습니다");
                    // location.reload();
                    window.location.href = response.redirectUrl;
                }else {
                    console.log("삭제 실패: ", response.message);
                    alert(response.message || "삭제에 실패했습니다.");
                }

            },
            error: function (xhr, status, error) {
                console.log("ajax 요청 실패: ", status,error);
                alert("삭제실패")
            }
        })

    }
}