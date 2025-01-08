let board_list = document.getElementById("board_list");
let notice_list = document.getElementById("notice_list");
let attendance_list = document.getElementById("attendance_list");
let personal_task_list = document.getElementById("personal_task_list");

let board_empty = document.getElementById("board_empty");
let notice_empty = document.getElementById("notice_empty");
let attendance_empty = document.getElementById("attendance_empty");
let personal_task_empty = document.getElementById("personal_task_empty");

window.onload = () => {
    if (board_list.rows.length <= 0) {
        board_empty.style.display = "";
    } else {
        board_empty.style.display = "none";
    }

    if (notice_list.rows.length <= 0) {
        notice_empty.style.display = "";
    } else {
        notice_empty.style.display = "none";
    }

    if (attendance_list.rows.length <= 0) {
        attendance_empty.style.display = "";
    } else {
        attendance_empty.style.display = "none";
    }

    if (personal_task_list.rows.length <= 0) {
        personal_task_empty.style.display = "";
    } else {
        personal_task_empty.style.display = "none";
    }
}
