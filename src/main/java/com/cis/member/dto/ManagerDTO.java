package com.cis.member.dto;

//import jakarta.validation.constraints.Pattern;

public class ManagerDTO {
    private String resident_num;    // 주민등록 번호
    private String emp_name;        // 사원이름
    private String emp_dept;        // 부서명
    //    private String emp_tel;         // 전화번호
    private String emp_rank;        // 직급
    private String join_date;       // 입사날짜
    private String work_status;     // 재직상태



    private String emp_id;

    public String getEmp_pass() {
        return emp_pass;
    }

    public void setEmp_pass(String emp_pass) {
        this.emp_pass = emp_pass;
    }

    private String emp_pass;

    public String getJoin_date() {
        return join_date;
    }

    public void setJoin_date(String join_date) {
        this.join_date = join_date;
    }

    public String getEmp_rank() {
        return emp_rank;
    }

    public void setEmp_rank(String emp_rank) {
        this.emp_rank = emp_rank;
    }

//    public String getEmp_tel() {
//        return emp_tel;
//    }
//
//    public void setEmp_tel(String emp_tel) {
//        this.emp_tel = emp_tel;
//    }

    public String getEmp_dept() {
        return emp_dept;
    }

    public void setEmp_dept(String emp_dept) {
        this.emp_dept = emp_dept;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getResident_num() {
        return resident_num;
    }

    public void setResident_num(String resident_num) {
        this.resident_num = resident_num;
    }

    public String getWork_status() {
        return work_status;
    }

    public void setWork_status(String work_status) {
        this.work_status = work_status;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    @Override
    public String toString() {
        return "ManagerDTO{" +
                "resident_num='" + resident_num + '\'' +
                ", emp_name='" + emp_name + '\'' +
                ", emp_dept='" + emp_dept + '\'' +
//                ", emp_tel='" + emp_tel + '\'' +
                ", emp_rank='" + emp_rank + '\'' +
                ", join_date='" + join_date + '\'' +
                ", work_status='" + work_status + '\'' +
                '}';
    }
}
