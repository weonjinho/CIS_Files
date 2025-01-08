package com.cis.member.service;

import com.cis.member.dto.EmployeeDTO;
import com.cis.member.dto.ManagerDTO;
import com.cis.member.dto.ManagerEmployeeDTO;
import com.cis.member.repository.IF_MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class MemberServiceImpl implements IF_MemberService{

    @Autowired
    IF_MemberDao memberdao;

    // 관리자 비밀번호.
//    String manager_password = "manager1234!@#";
    String manager_password = "1234";

    @Override
    public boolean check_manager_pass(String pass) {
        if(pass.equals(manager_password)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List<ManagerEmployeeDTO> total_employee_list(int startIndex, int pageSize) throws Exception {
        List<ManagerEmployeeDTO> list = memberdao.selectAll( startIndex,  pageSize);
        for(ManagerEmployeeDTO member : list){
            System.out.println("MemberServiceImpl_추가 필요한 사원들의 정보 전 : " + member.toString());
        }
        for(ManagerEmployeeDTO member : list){
            if (member.getEmp_id().equals("111")){
                member.setEmp_id("(정보 추가필요)");
            }
        }
        for(ManagerEmployeeDTO member : list){
            System.out.println("MemberServiceImpl_추가 필요한 사원들의 정보 후 : " + member.toString());
        }
        return list;
    }

    @Override
    public int count_id(String id) throws Exception {
        int count = memberdao.check_id(id);
        return count;
    }

    // 신규 사원 등록
    @Override
    public void add_new_employee(EmployeeDTO employeedto) throws Exception {
        System.out.println("MemberServiceImpl_입력한 값 : " + employeedto.toString());
        memberdao.insert_employee(employeedto);
    }

    @Override
    public List<ManagerEmployeeDTO> total_search_employee_list(String input_name) throws Exception {
        return memberdao.selectSearch(input_name);
    }

    @Override
    public int total_count_employee_list() throws Exception {
        int count_employee = memberdao.count_employee();
        System.out.println("전체 사원 인원수 : " + count_employee);
        return count_employee;
    }

    @Override
    public EmployeeDTO selectOne(String id) throws Exception {
        memberdao.selectOne(id);
        return memberdao.selectOne(id);
    }

    // 전체 사원 리스트에서 콤포박스에서 "부서"로 조회할때.
    @Override
    public List<ManagerEmployeeDTO> total_dept_list( String department, int startIndex, int pageSize) throws Exception {
        if(department.equals("business")){
            department = "영업팀";
        }else if(department.equals("program")){
            department = "개발팀";
        }else if(department.equals("technology")){
            department = "기술팀";
        }else if(department.equals("planning")){
            department = "기획팀";
        }else if(department.equals("accounting")){
            department = "회계팀";
        }else if(department.equals("human_resources")){
            department = "인사팀";
        }
        int count_selected_dept_employee = memberdao.total_selected_dept_employee_count(department);
        System.out.println("MemberServiceImpl_선택한 부서의 인원수 : " + count_selected_dept_employee);
        List<ManagerEmployeeDTO> list = memberdao.select_dept_list(department, startIndex, pageSize);
        return list;
    }

    // 전체 사원 리스트에서 콤포박스에서 "재직상태"로 조회할때.
    @Override
    public List<ManagerEmployeeDTO> total_work_status_list( String work_status, int startIndex, int pageSize) throws Exception {
        System.out.println("클라이언트에서 받아온값 ServiceImpl : " + work_status);
        if(work_status.equals("select_working")){
            work_status="재직중";
        }else if(work_status.equals("select_leave")){
            work_status="퇴사";
        }
        List<ManagerEmployeeDTO> list = memberdao.select_work_status_list(work_status, startIndex, pageSize);
        System.out.println("재직상태 선택시 DB 에서 받아온 사원수 : " + list.size());
        return list;
    }

    @Override
    public ManagerEmployeeDTO one_employee_info(String name) throws Exception {
        ManagerEmployeeDTO one_employee_info = memberdao.select_one_employee_info(name);
        System.out.println("DB 에서 받아온 한명에 사원에 모든 정보 : " + one_employee_info.toString());
        return one_employee_info;
    }

    @Override
    public ManagerEmployeeDTO login_employee_info(String id) throws Exception {
        System.out.println("MemberServiceImpl 에서 확인 : " + id);
        ManagerEmployeeDTO login_information = memberdao.select_login_employee_info(id);
        return login_information;
    }

    @Override
    public void modify_employee_info(ManagerEmployeeDTO employee) throws Exception {
        System.out.println("manager_modify_info 변경 전 : " + employee.toString());
        System.out.println("manager_modify_info 날짜 정보 : " + employee.getJoin_date());
        switch (employee.getEmp_dept()) {
            case "business" -> employee.setEmp_dept("영업팀");
            case "program" -> employee.setEmp_dept("개발팀");
            case "technology" -> employee.setEmp_dept("기술팀");
            case "planning" -> employee.setEmp_dept("기획팀");
            case "accounting" -> employee.setEmp_dept("회계팀");
            case "human_resources" -> employee.setEmp_dept("인사팀");
        }

        switch (employee.getEmp_rank()) {
            case "intern" -> employee.setEmp_rank("인턴");
            case "staff" -> employee.setEmp_rank("사원");
            case "assistant_manager" -> employee.setEmp_rank("대리");
            case "manager" -> employee.setEmp_rank("과장");
            case "team_manager" -> employee.setEmp_rank("팀장");
            case "director" -> employee.setEmp_rank("임원");
        }

        switch (employee.getWork_status()){
            case "재직중" -> employee.setWork_status("재직중");
            case "퇴사" -> employee.setWork_status("퇴사");
        }
        System.out.println("변경 후 값 확인 : " + employee.toString());
        memberdao.modify_employee_info(employee);
    }

    @Override
    public List<ManagerEmployeeDTO> get_need_complete_employee_list(int startIndex, int pageSize) throws Exception {
        List<ManagerEmployeeDTO> list = memberdao.select_manager_add_info(startIndex, pageSize);
        for(ManagerEmployeeDTO member : list){
            if (member.getEmp_name().equals("111")){
                member.setEmp_name("(추가필요)");
            }
            if(member.getEmp_rank().equals("111")){
                member.setEmp_rank("(추가필요)");
            }
            if(member.getEmp_dept().equals("111")){
                member.setEmp_dept("(추가필요)");
            }
            if(member.getJoin_date().equals("111")){
                member.setJoin_date("(추가필요)");
            }
            if (member.getWork_status().equals("111")){
                member.setWork_status("(추가필요)");
            }
        }
        for(ManagerEmployeeDTO member : list){
            System.out.println("추가 필요한 사원들의 정보 : " + member.toString());
        }
        return list;
    }

    @Override
    public ManagerEmployeeDTO select_one_employee_info_need_complete(String id) throws Exception {
        ManagerEmployeeDTO list = memberdao.select_one_employee_info_need_complete(id);
        System.out.println("555555");
        System.out.println("Dao 전 : " + list.toString());
        System.out.println("Dao 후 : " + list.toString());
        return list;
    }

    @Override
    public void complete_info(ManagerEmployeeDTO member) throws Exception {
        System.out.println("관리자가 보충한 데이터 : " + member.toString());
        switch (member.getEmp_dept()) {
            case "business" -> member.setEmp_dept("영업팀");
            case "program" -> member.setEmp_dept("개발팀");
            case "technology" -> member.setEmp_dept("기술팀");
            case "planning" -> member.setEmp_dept("기획팀");
            case "accounting" -> member.setEmp_dept("회계팀");
            case "human_resources" -> member.setEmp_dept("인사팀");
        }
        switch (member.getEmp_rank()) {
            case "intern" -> member.setEmp_rank("인턴");
            case "staff" -> member.setEmp_rank("사원");
            case "assistant_manager" -> member.setEmp_rank("대리");
            case "manager" -> member.setEmp_rank("과장");
            case "team_manager" -> member.setEmp_rank("팀장");
            case "director" -> member.setEmp_rank("임원");
        }
        System.out.println("MemberServiceImpl_변경 후 값 확인222 : " + member.toString());
        memberdao.update_complete_employee_info(member);
    }

    @Override
    public int total_count_number() throws Exception {
        return memberdao.total_employee_count();
    }

    @Override
    public int total_count_number_need_add_info() throws Exception {
        return memberdao.total_count_need_add_info_employee();
    }

    @Override
    public int total_count_selected_dept_employee(String dept) throws Exception {
        switch (dept) {
            case "business" -> dept = "영업팀";
            case "program" -> dept = "개발팀";
            case "technology" -> dept = "기술팀";
            case "planning" -> dept = "기획팀";
            case "accounting" -> dept = "회계팀";
            case "human_resources" -> dept = "인사팀";
        }
        System.out.println("MemberServiceImpl_콤보박스 선택한 부서명 : " + dept);
        return memberdao.total_selected_dept_employee_count(dept);
    }

    @Override
    public int total_count_selected_work_status_employee(String work_status) throws Exception {
        switch (work_status){
            case "select_working" -> work_status = "재직중";
            case "select_leave" -> work_status = "퇴사";
        }
        System.out.println("MemberServiceImpl_콤보박스 선택한 재직상태 : " + work_status);
        int count = memberdao.total_selected_work_status_employee_count(work_status);
        System.out.println("MemberServiceImpl_콤보박스 선택한 재직상태 인원수 : " + count);
        return count;
    }

    @Override
    public void add_new_employee_info(ManagerDTO member) throws Exception {
        System.out.println("MemberServiceImpl 진입확인 111 : " + member.toString());
        memberdao.manager_insert_new_employee_info(member);
    }

    @Override
    public void add_new_employee_rrn_in_employee(String rrn, String r_num) throws Exception {
        System.out.println("MemberServiceImpl_rrn_확인 : " + rrn);
        memberdao.insert_new_employee_rrn_in_employee(rrn, r_num);
    }

    @Override
    public List<ManagerEmployeeDTO> employee_need_complete(int startIndex, int pageSize) throws Exception {
        List<ManagerEmployeeDTO> list = memberdao.select_list_need_complete(startIndex,pageSize);
        for(ManagerEmployeeDTO member : list){
            System.out.println("MemberServiceImpl_추가 필요한 사원들의 정보 전 : " + member.toString());
        }
        for(ManagerEmployeeDTO member : list){
            if (member.getEmp_id().equals("111")){
                member.setEmp_id("(사원측 추가필요)");
            }
        }
        for(ManagerEmployeeDTO member : list){
            System.out.println("MemberServiceImpl_추가 필요한 사원들의 정보 후 : " + member.toString());
        }
        return list;
    }

    @Override
    public int total_count_employee_need_complete() throws Exception {
        return memberdao.count_employee_need_complete();
    }


    @Override
    public void manager_modify_info(String rrn, String work_status) throws Exception {
        memberdao.modify_employee_info(rrn,work_status);
    }


}
