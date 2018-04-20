package com.vranec.controller;

import com.vranec.jpa.model.IssueModel;
import com.vranec.jpa.repository.TimeLogRepository;
import com.vranec.view.TaskViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TasksController {

    @Autowired
    TimeLogRepository timeLogRepo;

    @RequestMapping(value = "/tasks")
    public String showTasksPage() {
        return "tasks";
    }

    @RequestMapping(value = "/tasks-table")
    @ResponseBody
    public List<TaskViewModel> getTasksTable(){
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
                .status(issueModel.getStatus())
                .workload(minutes / 60.0)
                .build();
    }
}
