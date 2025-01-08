
document.addEventListener("DOMContentLoaded", () => {
    let count =0;

    //파일 추가버튼 클릭시
    let file = document.querySelector("#filebtn").addEventListener("click", ()=> {


        const fileList = document.querySelector(".write_files")

        //새로운 파일 입력 요소 추가
        let newfile = document.createElement("div");
        newfile.classList.add('file_add',);
        newfile.innerHTML = '<input type="file" name="file" id="formFile"><input type="button" value="취소" id="file_cancelbtn">';
        fileList.appendChild(newfile);

        //파일 개수 증가 및 버튼 비활성화
        count++;
        if(count == 3) {
            document.querySelector("#filebtn").disabled = true;
        }

    });

    //파일 삭제버튼 클릭시
    let write_files_div = document.querySelector(".write_files").addEventListener("click",(event)=> {

        let filediv = document.querySelector(".file_add");
        let filebtn  = document.querySelector("#filebtn");
        if(event.target.id === 'file_cancelbtn') {
            filediv = filediv.remove();
            count--;
            if(count < 3) {
                filebtn.disabled = false;
            }

        }
    });


});

