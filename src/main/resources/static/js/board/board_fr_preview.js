const titlebox = document.querySelector("#titlebox");
const contextbox = document.querySelector("#contextbox");
const categoryIdbox = document.querySelector("#categoryId");
const nameBox = document.querySelector("#namebox");
const addButton = document.querySelector("#addbutton");
const fileListUl = document.getElementById("fileListUl");

const MAX_FILES = 3;
const pathname = window.location.pathname;
const num = pathname.split('/').pop(); // 게시글 번호

// 삭제된 파일 ID를 저장할 배열
let deletedFileIds = [];

// 목록 버튼 클릭
function btnlist() {
    // location.href = "/board_gj";
    history.back();
}


//삭제버튼 클릭시
document.querySelector("#delbtn").addEventListener("click", ()=> {
    // const url = window.location.href; // url 가져오기
    //  let query = window.location.search; // url query 가져오기
    // let param = new URLSearchParams(query);
    // alert(num);

    //formdata 생성
    const formdata = new FormData;
    formdata.append("category", categoryIdbox.value); // boardVO.category


    //삭제할 file의 id 추가
    const listItems = document.querySelectorAll("li[data-file-id]");
    // 각 <li>의 data-file-id 값 출력
    listItems.forEach((item) => {
        const fileId = item.getAttribute("data-file-id");
        formdata.append("delfileIds", fileId);

    });

    // // 디버깅용 출력
    // for (let [key, value] of formdata.entries()) {
    //     console.log(`${key}: ${value}`);
    // }

    fetch(`/fr_preview/delOne/${num}`, {
        method: "POST",
        body: formdata,
    }).then((response) => {
        if (response.ok) {

            alert("삭제가 완료되었습니다")
            window.location.href = "/board_fr"; // 목록 페이지로 리다이렉트

        } else {
            alert("삭제에 실패했습니다")
        }
    })
        .catch((error) => {
            alert("에러가 발생했습니다. 다시 시도해주세요")
        })

});




// 수정 버튼 클릭 이벤트
document.querySelector("#modifybtn").addEventListener("click", () => {
    titlebox.removeAttribute("readonly");
    contextbox.removeAttribute("readonly");
    const firstli = document.querySelector(".add_1_li");
    addButton.hidden = false;
    firstli.style.display = "block";
    // 기존 파일 목록에 삭제 버튼 추가
    const fileItems = fileListUl.querySelectorAll("li");
    fileItems.forEach((item) => {
        if (item.querySelector("#addbutton")) return;

        if (!item.querySelector(".deleteFileBtn")) {
            const deleteBtn = document.createElement("button");
            deleteBtn.type = "button";
            deleteBtn.textContent = "삭제";
            deleteBtn.classList.add("deleteFileBtn");
            deleteBtn.setAttribute("data-file-id", item.getAttribute("data-file-id"));
            item.appendChild(deleteBtn);
        }
    });

    // 삭제 버튼 이벤트 등록
    setupDeleteButtons();

    // 수정/취소 버튼 변경
    const updateDiv = document.querySelector(".update_delete_div");
    updateDiv.innerHTML = `
        <input type="button" id="saveBtn" value="저장">
        <input type="button" id="cancelBtn" value="취소">
    `;

    // 저장 및 취소 버튼 이벤트 등록
    setupSaveCancelEvents();
});

// // 수정 버튼 클릭 후 삭제 버튼 이벤트 동적 등록
function setupDeleteButtons() {
    const deleteFileButtons = document.querySelectorAll(".deleteFileBtn");
    console.log("삭제 버튼 개수:", deleteFileButtons.length);

    deleteFileButtons.forEach((btn, index) => {
        btn.addEventListener("click", () => {
            const fileId = btn.getAttribute("data-file-id");
            console.log(`삭제 버튼 ${index + 1} 클릭됨, 파일 ID: ${fileId}`);
            if (fileId) {
                // 삭제된 파일 ID를 배열에 추가
                deletedFileIds.push(fileId);
                console.log("삭제된 파일 ID 목록:", deletedFileIds); // 디버깅
            }
            btn.closest("li").remove(); // 파일 삭제 시 DOM에서도 제거
        });
    });
}

// 파일 추가 버튼 클릭 이벤트
addButton.addEventListener("click", () => {
    const currentFileCount = fileListUl.querySelectorAll("li:not(:has(#addbutton))").length;

    if (currentFileCount >= MAX_FILES) {
        alert(`최대 ${MAX_FILES}개의 파일만 업로드 가능합니다.`);
        return;
    }

    const newFileLi = document.createElement("li");
    const newFileInput = document.createElement("input");
    newFileInput.type = "file";
    newFileInput.name = "files";
    newFileLi.appendChild(newFileInput);
    fileListUl.insertBefore(newFileLi, fileListUl.lastElementChild);
});

// 저장 및 취소 버튼 이벤트 설정
function setupSaveCancelEvents() {
    document.querySelector("#saveBtn").addEventListener("click", handleSave);
    document.querySelector("#cancelBtn").addEventListener("click", handleCancel);
}

// 저장 버튼 처리 함수
function handleSave() {
    const formData = new FormData();

    // boardVO 데이터 추가
    formData.append("board_title", titlebox.value);
    formData.append("board_content", contextbox.value);
    formData.append("category", categoryIdbox.value);
    formData.append("emp_id", nameBox.value);
    formData.append("board_num", num);

    // 삭제된 파일 정보 추가
    deletedFileIds.forEach((fileId) => {
        formData.append("deleteFiles", fileId);
        console.log("FormData에 추가된 삭제 파일 ID:", fileId);
    });

    // 새로운 파일 추가
    const newFileInputs = fileListUl.querySelectorAll("input[type='file']");
    newFileInputs.forEach((input) => {
        if (input.files.length > 0) {
            formData.append("files", input.files[0]);
            console.log("FormData에 추가된 새 파일:", input.files[0].name);
        }
    });


    // 서버로 데이터 전송
    fetch(`/fr_preview/modifyOne/${num}`, {
        method: "POST",
        body: formData,
    })
        .then((response) => {
            if (response.ok) {
                alert("수정이 완료되었습니다.");
                location.reload();
            } else {
                alert("수정에 실패했습니다.");
            }
        })
        .catch((error) => {
            console.error("Error:", error);
            alert("에러가 발생했습니다. 다시 시도해주세요.");
        });
}

// 취소 버튼 처리 함수
function handleCancel() {
    alert("수정을 취소합니다.");
    location.reload(true);
}

// 초기화: 페이지 로드 시 삭제 버튼 등록
document.addEventListener("DOMContentLoaded", () => {
    setupDeleteButtons();
});
