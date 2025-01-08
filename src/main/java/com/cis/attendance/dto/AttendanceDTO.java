package com.cis.attendance.dto;

public class AttendanceDTO {
    private String emp_id;
    private String work_start;
    private String work_end;
    private String work_date;
    private String significant;
    private String work_total;
    private String late_check;

    public String getEmp_id() {
        return emp_id;
    }
    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }
    public String getWork_start() {
        return work_start;
    }
    public void setWork_start(String work_start) {
        this.work_start = work_start;
    }
    public String getWork_end() {
        return work_end;
    }
    public void setWork_end(String work_end) {
        this.work_end = work_end;
    }
    public String getWork_date() {
        return work_date;
    }
    public void setWork_date(String work_date) {
        this.work_date = work_date;
    }
    public String getSignificant() {
        return significant;
    }
    public void setSignificant(String significant) {
        this.significant = significant;
    }
    public String getWork_total() {
        return work_total;
    }
    public void setWork_total(String work_total) {
        this.work_total = work_total;
    }
    public String getLate_check() {
        return late_check;
    }
    public void setLate_check(String late_check) {
        this.late_check = late_check;
    }

    @Override
    public String toString() {
        return "AttendanceDTO{" +
                "emp_id='" + emp_id + '\'' +
                ", work_start='" + work_start + '\'' +
                ", work_end='" + work_end + '\'' +
                ", work_date='" + work_date + '\'' +
                ", significant='" + significant + '\'' +
                ", work_total='" + work_total + '\'' +
                ", late_check='" + late_check + '\'' +
                '}';
    }

}
