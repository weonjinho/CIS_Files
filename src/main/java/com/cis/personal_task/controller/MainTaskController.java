package com.cis.personal_task.controller;

import com.cis.personal_task.dto.PersonalTaskDTO;
import com.cis.personal_task.dto.TaskFileDTO;
import com.cis.personal_task.service.PersonalTaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainTaskController {
    private final PersonalTaskService personalTaskService;

    public MainTaskController(PersonalTaskService personalTaskService) {
        this.personalTaskService = personalTaskService;
    }

    @GetMapping("/tasks")
    public String showMainPage(
            @RequestParam(value = "task_status", required = false, defaultValue = "전체보기") String task_status,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            Model model) {

        // 페이지 당 보여줄 항목 수 설정
        int pageSize = 10;
        PageHelper.startPage(page, pageSize);

        // 상태에 따른 업무 리스트 가져오기
        List<PersonalTaskDTO> tasks = personalTaskService.getTasksByStatus(task_status);

        // 페이지 정보 생성
        PageInfo<PersonalTaskDTO> pageInfo = new PageInfo<>(tasks);

        // 모델에 데이터 추가
        model.addAttribute("tasks", tasks);
        model.addAttribute("currentPage", pageInfo.getPageNum());
        model.addAttribute("totalPages", pageInfo.getPages());
        model.addAttribute("task_status", task_status);

        return "personal_task/task_main";
    }

    // 받은 업무 리스트 조회 (Thymeleaf View)
    @GetMapping("/received-view")
    public String getReceivedTasksView(Model model, HttpSession session) {
        String receiveId = (String) session.getAttribute("emp_id");
        if (receiveId == null) {
            throw new IllegalStateException("로그인되지 않았습니다.");
        }
        try {
            List<PersonalTaskDTO> receivedTasks = personalTaskService.getReceivedTasks(receiveId);
            model.addAttribute("receivedTasks", receivedTasks);
            return "personal_task/task_main"; // Thymeleaf 템플릿 이름
        } catch (Exception e) {
            throw new IllegalStateException("받은 업무를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/detail/{taskNum}")
    public String getTaskDetails(@PathVariable int taskNum, Model model) {
        try {
            PersonalTaskDTO taskDetail = personalTaskService.getTaskDetails(taskNum);
            List<TaskFileDTO> taskFiles = personalTaskService.getTaskFiles(taskNum);

            model.addAttribute("taskDetail", taskDetail);
            model.addAttribute("taskFiles", taskFiles);

            return "personal_task/task_detail"; // Thymeleaf 템플릿 이름
        } catch (Exception e) {
            model.addAttribute("errorMessage", "업무를 찾을 수 없습니다.");
            return "personal_task/task_main";
        }
    }
}
