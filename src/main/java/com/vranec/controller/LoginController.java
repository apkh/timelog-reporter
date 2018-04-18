package com.vranec.controller;

import com.vranec.jpa.model.IssueModel;
import com.vranec.jpa.repository.TimeLogRepository;
import com.vranec.view.TaskViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
//@SessionAttributes("name")
public class LoginController {

    @Autowired
    LoginService service;

    @Autowired
    TimeLogRepository timeLogRepo;

    @RequestMapping(value="/login", method = RequestMethod.GET)
    @ResponseBody
    public List<TaskViewModel> showLoginPage(){
        List<Object[]> tasks = timeLogRepo.getTasksWithWorkload();
        List<TaskViewModel> t = tasks.stream()
                .filter(elt -> elt != null && elt.length == 2)
                .map(elt -> taskToViewModel((IssueModel) elt[0], (Long) elt[1]))
                .collect(Collectors.toList());
        //model.put("tasks", t.toArray());
        return t;
    }

    private TaskViewModel taskToViewModel(IssueModel issueModel, Long minutes) {
        return TaskViewModel.builder()
                .description(issueModel.getSummary())
                .key(issueModel.getIssueId())
                .assignee(issueModel.getAssignee())
                .workload(minutes / 60.0)
                .build();
    }

    @RequestMapping(value="/login", method = RequestMethod.POST)
    public String showWelcomePage(ModelMap model, @RequestParam String name, @RequestParam String password){

        boolean isValidUser = service.validateUser(name, password);

        if (!isValidUser) {
            model.put("errorMessage", "Invalid Credentials");
            return "login";
        }

        model.put("name", name);
        model.put("password", password);

        return "welcome";
    }

}