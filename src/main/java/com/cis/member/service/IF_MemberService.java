package com.cis.member.service;

import com.cis.member.dto.EmployeeDTO;
import com.cis.member.dto.ManagerDTO;
import com.cis.member.dto.ManagerEmployeeDTO;
import com.cis.member.dto.PageDTO;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Map;

public interface IF_MemberService {

    // 관리자 비밀번호 확인.
    public boolean check_manager_pass(String pass) throws Exception;

    // 전체 사원 리스트 조회.
    public List<ManagerEmployeeDTO> total_employee_list(int startIndex, int pageSize) throws Exception;

    // 아이디 중복 체크.
    public int count_id(String id) throws Exception;

    // 신규 사원 등록.
    public void add_new_employee(EmployeeDTO employeedto) throws Exception;

    // 전체 직원 리스트에서 검색한 결과.
    public List<ManagerEmployeeDTO> total_search_employee_list(String input_name) throws Exception;

    // 페이징.
    public int total_count_employee_list() throws Exception;

    // 일반 사원 로그인.
    public EmployeeDTO selectOne(String id) throws Exception;

    // 콤보박스에서 부서 선택시.
    public List<ManagerEmployeeDTO> total_dept_list( String department, int startIndex, int pageSize) throws Exception;

    // 콤보박스에서 재직상태 선택시.
    public List<ManagerEmployeeDTO> total_work_status_list( String work_status, int startIndex, int pageSize) throws Exception;

    // 전체 사원 리스트에서 사원에 이름을 클릭했을때, 그 사원에 모든 정보를 조회.
    public ManagerEmployeeDTO one_employee_info(String name) throws Exception;

    // 전체 사원 리스트에서 사원에 이름을 클릭했을때, 그 사원에 모든 정보를 조회.
    public ManagerEmployeeDTO login_employee_info(String id) throws Exception;

    // 관리자가 추가한 사원에 정보.
    public void modify_employee_info(ManagerEmployeeDTO employee) throws Exception;

    // 관리자가 정보를 추가할 필요가 있는 사원 리스트.
    public List<ManagerEmployeeDTO> get_need_complete_employee_list( int startIndex, int pageSize) throws Exception;

    // 관리자가 정보 추가를 필요한 사원 한명에 정보를 조회.
    public ManagerEmployeeDTO select_one_employee_info_need_complete(String id) throws Exception;

    //  관리자가 보충한 데이터를 가공.
    public void complete_info(ManagerEmployeeDTO member) throws Exception;

    // *** 페이징 관련 ***
    // 정보 추가가 필요없는 전체 사원들의 totalCount 가져옴
    public int total_count_number()throws Exception;

    // 정보 추가가 필요한 사원들의 totalCount
    public int total_count_number_need_add_info() throws Exception;

    // 콤보박스에서 "부서"를 선택한 사원의 인원수
    public int total_count_selected_dept_employee(String dept) throws Exception;

    // 콤보박스에서 "재직상태"를 선택한 사원의 인원수
    public int total_count_selected_work_status_employee(String work_status) throws Exception;

    // =========================
    // 관리자 신규 사원 등록
    public void add_new_employee_info(ManagerDTO member) throws Exception;

    // 사원테이블에 관리자가 입력한 rrn 삽입.
    public void add_new_employee_rrn_in_employee(String rrn,String r_num) throws Exception;

    // 사원이 정보를 환성시킬 필욯가 있는 사원의 리스트
    public List<ManagerEmployeeDTO> employee_need_complete(int startIndex, int pageSize) throws Exception;


    public int total_count_employee_need_complete() throws Exception;

    // 관리자 사원 퇴사처리_원진호_1213
    public void manager_modify_info(String rrn, String work_status) throws Exception;

}