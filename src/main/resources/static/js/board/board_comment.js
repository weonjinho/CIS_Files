document.addEventListener("DOMContentLoaded", ()=> {
    const commentForm = document.getElementById("commentForm");
    const commentList = document.querySelector("#CommentList");
    const categoryIdbox = document.querySelector("#categoryId");
    const pathname = window.location.pathname;
    const num = pathname.split('/').pop(); // 게시글 번호

    const checkCommentBtn = document.getElementById("check_comment");
    const commentModal = document.getElementById("commentModal");
    const closeModal = document.getElementById("closeModal");
    const modalCommentList = document.getElementById("modalCommentList");
    const modalCommentForm = document.getElementById("modalCommentForm");

    // 모달 열기
    checkCommentBtn.addEventListener("click", () => {
        commentModal.style.display = "block";
        loadComments(); // 댓글 로드
    });

    // 모달 닫기
    closeModal.addEventListener("click", () => {
        commentModal.style.display = "none";
    });

    // 댓글 등록
    modalCommentForm.addEventListener("submit", (e) => {
        e.preventDefault();
        // const formData = new FormData(modalCommentForm);
        // formData.append("category", categoryIdbox.value);
        // formData.append("board_num", num);

        const formData = new FormData(modalCommentForm);

        const requestData= {};
        formData.forEach((value, key) => {
            requestData[key] = value;
        });
        //추가 데이터 삽입
        requestData["category"] = categoryIdbox.value;
        requestData["board_num"] = num;

        fetch(`/board/addComment/${num}`, {
            method:"POST",
            headers: {
                "content-type": "application/json",
            },
            body: JSON.stringify(requestData),
        })
            .then((response) => response.json())
            .then((data) => {
                if(data.success) {
                    alert("댓글이 등록 되었습니다");
                    modalCommentForm.reset();
                    loadComments();
                }else{
                    alert("댓글 등록 실패")
                }
            })
            .catch((error)=> console.log("Error", error))

    });

    // 댓글 로드
    function loadComments() {
        const category = categoryIdbox.value;
        fetch(`/board/getComment/${category}/${num}`)
            .then((response) => response.json())
            .then((data) => {
                if (data.success) {
                    //임시아이디 설정. 나중에 확인할것 // 서버에서 받음
                    const currentUserId = data.current_user_id;
                    console.log("current user id : /// " + currentUserId);
                    const adminFlag = data.admin_flag;
                    console.log("  admin flag :  " + adminFlag);
                    modalCommentList.innerHTML = ""; // 기존 댓글 초기화
                    data.comments.forEach((comment) => {
                        const commentItem = document.createElement("div");
                        commentItem.className = "comment_item";
                        console.log("커렌트 유저 아이디: "+ currentUserId + "  어드민 플래그 :  " + adminFlag + "댓글 emp id : " + comment.emp_id);

                        commentItem.innerHTML = `
                             <p><strong>${comment.emp_name}</strong>: </p>
                             <p data-comment-id="${comment.comment_num}"><strong>${comment.comment_content}</strong></p>
                             <p>${comment.create_at}</p>
                             <p hidden="hidden"><strong>${comment.emp_id}</strong> :</p>
                            ${
                            comment.emp_id === currentUserId || adminFlag === true
                                ? `<button class="delete-btn" data-comment-id="${comment.comment_num}">삭제</button>
                                    `
                                : ""
                        }
                        `;
                        modalCommentList.appendChild(commentItem);
                    });
                    // <button className="edit-btn" data-comment-id="${comment.comment_num}">수정</button>

                    // 삭제 버튼 이벤트 추가
                    document.querySelectorAll(".delete-btn").forEach((button) => {
                        button.addEventListener("click", (e) => {
                            const commentId = e.target.dataset.commentId;
                            deleteComment(commentId);
                        });
                    });
                    //수정 버튼 이벤트 추가
                    document.querySelectorAll(".edit-btn").forEach((button) => {
                        button.addEventListener("click", (e) => {
                            const commentId = e.target.dataset.commentId;
                            const commentItem = e.target.closest(".comment_item")
                            //[data-cmment-id]가 있는 p태그
                            const commentContentEl = commentItem.querySelector(`p[data-comment-id="${commentId}"]`);
                            if (!commentContentEl) {
                                console.log(commentContentEl.textContent);
                                console.error("엘레먼트 없음")
                                return;
                            } else {
                                console.log(commentContentEl.textContent);
                            }
                            const commentContent = commentContentEl.textContent.trim()//댓글내용
                            alert(commentContent);
                            //버튼 클릭시 data-comment-id, .commet_item(div), (p태그의 댓글내용)
                            editComment(commentId, commentItem, commentContent, commentContentEl);
                        });
                    });
                } else {
                    alert("댓글 데이터를 가져오지 못했습니다.");
                }
            })
            .catch((error) => console.error("Error:", error));
    }

    // 댓글 삭제
    function deleteComment(commentId) {
        const category = categoryIdbox.value; // 현재 카테고리 값
        fetch(`/board/deleteComment/${category}/${commentId}`, { method: "DELETE" })
            .then((response) => response.json())
            .then((data) => {
                if (data.success) {
                    alert("댓글이 삭제되었습니다.");
                    loadComments(); // 삭제 후 목록 새로고침
                } else {
                    alert("댓글 삭제 실패!");
                }
            })
            .catch((error) => console.error("Error:", error));
    }

});