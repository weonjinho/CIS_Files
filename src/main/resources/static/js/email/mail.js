let mail_row = document.getElementById("mail_content");
let mail_empty = document.getElementById("mail_empty");

window.onload = () => {
    if (mail_row.rows.length <= 0) {
        mail_empty.style.display = "";
    } else {
        mail_empty.style.display = "none";
    }
}

let mail_all_check = document.getElementsByClassName("all_check")[0];
mail_all_check.addEventListener(("change"), () => {
    for (let i=0; i<mail_row.rows.length; i++) {
        mail_row.rows[i].cells[0].children[0].checked = mail_all_check.checked
    }
});

document.querySelectorAll(".one_check").forEach((check) => {
    check.addEventListener("change", () => {
        for (let i=0; i<mail_row.rows.length; i++) {
            if (!(mail_row.rows[i].cells[0].children[0].checked)) {
                mail_all_check.checked = false;
                return;
            }
            mail_all_check.checked = true;
        }
    });
});

let mail_delete = document.getElementsByClassName("mail_delete")[0]
mail_delete.addEventListener("click", () => {
    let mail_check = document.querySelectorAll(".one_check:checked");
    let mail_check_count = mail_check.length;

    if (mail_check_count <= 0) {
        alert("삭제할 메일을 선택해주세요.");
        return;
    }

    if ((confirm("정말로 삭제하시겠습니까?"))) {
        const mail_num_list = new Array(mail_check_count);
        mail_check.forEach((mail_num, index) => {
            mail_num_list[index] = mail_num.value;
        });

        const form = document.createElement("form");
        form.setAttribute("method", "post");
        form.setAttribute("action", "email_delete");

        let input = document.createElement("input");
        input.setAttribute("type", "hidden");
        input.setAttribute("name", "num");
        input.setAttribute("value", mail_num_list);

        form.appendChild(input);
        document.body.appendChild(form);
        form.submit();

        for (let i=0; i<mail_row.rows.length; i++) {
            if (mail_row.rows[i].cells[0].children[0].checked) {
                mail_row.deleteRow(i);
                i--;
            }
        }
    } else {
        return;
    }

    if (mail_row.rows.length <= 0) {
        mail_empty.style.display = "";
    } else {
        mail_empty.style.display = "none";
    }

    mail_all_check.checked = false;
});
