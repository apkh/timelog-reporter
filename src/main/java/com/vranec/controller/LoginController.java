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

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String showLoginPage(){
        return "login";
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

        return "tasks";
    }

}