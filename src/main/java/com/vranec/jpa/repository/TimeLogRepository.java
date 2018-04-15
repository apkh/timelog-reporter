package com.vranec.jpa.repository;

import com.vranec.jpa.model.IssueModel;
import com.vranec.jpa.model.TimeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TimeLogRepository extends JpaRepository<TimeLog, Long> {

    List<Integer> allDays();


    @Query("SELECT sum(tl.reportTime) FROM TimeLog tl where tl.resource=:user and tl.day=:date")
    Integer getWorkload(String user, Integer date);

    @Query("SELECT tl.issue.issueId FROM TimeLog tl where tl.resource=:user and tl.day=:date")
    List<String> getIssues(String user, Integer date);

    @Query("SELECT tl.issue,sum(tl.reportTime) FROM TimeLog tl group by tl.issue order by tl.resource")
    List<Object[]> getTasksWithWorkload();

    @Query("SELECT tl.resource,sum(tl.reportTime) FROM TimeLog tl group by tl.resource")
    List<Object[]> getStatisticsByResource();
}

