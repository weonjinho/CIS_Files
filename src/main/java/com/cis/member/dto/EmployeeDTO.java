package com.cis.member.dto;

public class EmployeeDTO {
    private String emp_id;          // 아이디
    private String resident_num;    // 주민등록번호
    private String emp_pass;        // 비밀번호
    private String emp_email;       // 사원 이메일 주소
    private String post_addr;       // 우편주소
    private String road_addr;       //  도로명 주소
    private String detail_addr;     // 상세주소


    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getResident_num() {
        return resident_num;
    }

    public void setResident_num(String resident_num) {
        this.resident_num = resident_num;
    }

    public String getEmp_pass() {
        return emp_pass;
    }

    public void setEmp_pass(String emp_pass) {
        this.emp_pass = emp_pass;
    }

    public String getEmp_email() {
        return emp_email;
    }

    public void setEmp_email(String emp_email) {
        this.emp_email = emp_email;
    }

    public String getPost_addr() {
        return post_addr;
    }

    public void setPost_addr(String post_addr) {
        this.post_addr = post_addr;
    }

    public String getRoad_addr() {
        return road_addr;
    }

    public void setRoad_addr(String road_addr) {
        this.road_addr = road_addr;
    }

    public String getDetail_addr() {
        return detail_addr;
    }

    public void setDetail_addr(String detail_addr) {
        this.detail_addr = detail_addr;
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "resident_num='" + resident_num + '\'' +
                ", emp_id='" + emp_id + '\'' +
                ", emp_pass='" + emp_pass + '\'' +
                ", employee_email='" + emp_email + '\'' +
                ", post_addr='" + post_addr + '\'' +
                ", road_addr='" + road_addr + '\'' +
                ", detail_addr='" + detail_addr + '\'' +
                '}';
    }
}
