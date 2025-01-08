function select_file(this_element) {
    let file = this_element.files[0];
    let file_name = this_element.closest(".mail_file").firstElementChild;

    if (!(file)) {
        file_name.value = null;
        return;
    }

    let file_extension = file.name.substring(file.name.length - 4);
    let extension_check_list = [".txt", ".gif", ".jpg", ".png", ".mp3", ".avi", ".mkv", ".zip"];
    let extension_check = false;

    for (let i=0; i<extension_check_list.length; i++) {
        if (extension_check_list.includes(file_extension)) {
            extension_check = true;
            break;
        }
    }

    if (!(extension_check)) {
        alert("등록할 수 있는 확장자명이 아닙니다.\n.zip 파일로 압축하거나 확장자명을 확인해주세요.");
        file_name.value = null;
        this_element.value = null;
        return;
    }

    let file_size = Math.floor(file.size / 1024 / 1024);

    if (file_size > 10) {
        alert("올릴 수 있는 파일의 크기는 10MB까지 입니다.");
        file_name.value = null;
        this_element.value = null;
        return;
    }
}

// 키를 뗀 경우의 이벤트, 제목 20글자 제한
let mail_title = document.getElementsByName("mail_title")[0];
mail_title.addEventListener(("keyup"), () => {
    if (mail_title.value.length > 20) mail_title.value = mail_title.value.substring(0, 20);
});

// 키를 뗀 경우의 이벤트, 받는 사람 아이디 12글자 제한, 정규식(공백,한글,대문자 제거)적용
let recipient_id = document.getElementsByName("recipient_id")[0];
let recipient_name_box = document.getElementsByClassName("mail_recipient_check_name")[0];
recipient_id.addEventListener("keyup", () => {
    if (recipient_id.value.length > 12) recipient_id.value = recipient_id.value.substring(0, 12);
    recipient_id.value = recipient_id.value.replaceAll(/[\sㄱ-ㅎㅏ-ㅣ가-힣A-Z]/g, "");

    let recipient_id_text = recipient_id.value;

    $.ajax({
        type : "POST",
        url : "/recipient_id/name/check",
        data : {
            "recipient_id" : recipient_id_text
        },
        success : function(name) {
            if (name.length <= 0) {
                if (recipient_id.value.length <= 0) {
                    recipient_name_box.style.color = "#666666"
                    recipient_name_box.innerText = "받는 사람의 아이디를 입력해주세요.";
                    return;
                }
                recipient_name_box.style.color = "red"
                recipient_name_box.innerText = "해당 아이디를 가진 유저는 존재하지 않습니다."
            } else {
                if (recipient_id_text === 'admin') {
                    recipient_name_box.style.color = "red"
                    recipient_name_box.innerText = "관리자에게는 보낼 수 없습니다."
                } else {
                    recipient_name_box.style.color = "dodgerblue"
                    recipient_name_box.innerText = "받는 사람 : " + name;
                }
            }
        },
        error : function() {
            alert("서버 요청 실패");
        }
    });
});

// 키를 뗀 경우의 이벤트, 내용 255글자 제한
let mail_content = document.getElementsByName("mail_content")[0];
mail_content.addEventListener("keyup", () => {
    if (mail_content.value.length > 255) mail_content.value = mail_content.value.substring(0, 255);
});

let send_btn = document.getElementsByClassName("send_btn")[0];
let email_send = document.getElementById("email_send");
send_btn.addEventListener("click", () => {
    let mail_title_text = mail_title.value;
    let recipient_id_text = recipient_id.value;

    if (mail_title_text.length <= 0 || mail_title_text.replaceAll(" ", "").length <= 0) {
        alert("제목을 입력해주세요.");
        mail_title.value = null;
        return;
    }

    if (recipient_id_text.length <= 0) {
        alert("받는 사람을 입력해주세요.");
        recipient_id.value = null;
        return;
    }

    if (recipient_id.value === 'admin') {
        alert("관리자에게는 보낼 수 없습니다.");
        recipient_id.value = null;
        recipient_name_box.style.color = "#666666"
        recipient_name_box.innerText = "받는 사람의 아이디를 입력해주세요.";
        return;
    }

    $.ajax({
        type : "POST",
        url : "/recipient_id/check",
        data : {
            "recipient_id" : recipient_id_text
        },
        success : function(check) {
            if (check === 1) {
                alert(recipient_id_text + "님은 존재하지 않습니다.\n아이디를 다시 확인해주세요.");
                recipient_id.value = null;
                recipient_name_box.style.color = "#666666"
                recipient_name_box.innerText = "받는 사람의 아이디를 입력해주세요.";
            } else {
                email_send.submit();
            }
        },
        error : function() {
            alert("서버 요청 실패");
        }
    });
});
