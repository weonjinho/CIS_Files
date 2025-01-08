package com.cis.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
* 관리자 사원 정보 수정화면 중 부서 콤보박스에서 사용.
*
* 부서명 :
* 영업팀 : business
* 개발팀 : program
* 기술팀 : technology
* 기획팀 : planning
* 회계팀 : accounting
* 인사팀 : human_resources
*
* */
@Data
@AllArgsConstructor
public class DepartmentCode {
    private String dept_code;
    private String dept_name;
}
