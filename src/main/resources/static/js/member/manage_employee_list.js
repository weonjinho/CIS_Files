let list_row = document.getElementById('employee_list_content');
let list_empty = document.getElementById('employee_list_empty');

window.onload = () => {
    if(list_row.rows.length <= 0){
        list_empty.style.display = "";
        console.log('보여짐');
    }else{
        list_empty.style.display = "none";
        console.log('숨김');
    }
}

