package com.vranec.controller;

import com.vranec.jpa.repository.TimeLogRepository;
import com.vranec.timesheet.generator.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Controller
public class WorkloadController {

    @Autowired
    private Configuration configuration;
    @Autowired
    TimeLogRepository timeLogRepo;

    @RequestMapping(value = "/workload")
    public String getWorkload() {
        return "workload";
    }

    @RequestMapping(value = "/wl-table")
    @ResponseBody
    public List<ArrayList<Object>> getWlTable(){
        List<Integer> daysNumber = timeLogRepo.allDays();
        List<ArrayList<Object>> result = new ArrayList(daysNumber.size());

        double[] totalWl = new double[configuration.getResources().size()];

        for (Integer date: daysNumber) {
            ArrayList<Object> currentLine = new ArrayList<>();
            result.add(currentLine);

            currentLine.add(date.toString());

            int counter = 0;
            for (String user: configuration.getResources()) {
                Integer workload = timeLogRepo.getWorkload(user, date);
                List<String> issues = timeLogRepo.getIssues(user, date);
                double wl = ((workload == null) ? 0 : workload) / 60.0;
                totalWl[counter++] += wl;
                currentLine.add(String.valueOf(wl));
                currentLine.add(String.join(",", issues));
            }
        }
        final ArrayList<Object> totalLine = new ArrayList<>();
        totalLine.add("");
        Arrays.stream(totalWl).forEach(wl ->{
            totalLine.add(wl);
            totalLine.add("");
        });
        result.add(totalLine);

        return result;
    }

    @RequestMapping(value = "/wl-header")
    @ResponseBody
    public ArrayList<HashMap> getWlHeader(){
        ArrayList<HashMap> result = new ArrayList<>();

        result.add(column("Date", "0"));
        int counter = 1;
        for (String user: configuration.getResources()) {
            result.add(column(user + "'s wl", String.valueOf(counter++)));
            result.add(column(user + "'s tasks", String.valueOf(counter++)));
        }
        return result;
    }

    private HashMap column(String title, String field) {
        HashMap<String, String> result = new HashMap<>(2);
        result.put("title", title);
        result.put("field", field);
        return result;

    }


}
