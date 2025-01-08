package com.cis.member.controller;

import com.cis.member.service.IF_MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class ManagerController {

    @Autowired
    IF_MemberService memberService;

    @PostMapping(value="ajaxStr")
    public Object check_pass(@RequestBody String emp_id) throws Exception{
//        System.out.println("ManagerController 진입 111");
        String[] a = emp_id.split("=");
        String result = a[1];
        int check_return = memberService.count_id(result);
//        System.out.println("ManagerController 진입 222");
        return check_return;
    }
}
