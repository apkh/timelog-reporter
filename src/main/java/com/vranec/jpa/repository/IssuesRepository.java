package com.vranec.jpa.repository;

import com.vranec.jpa.model.IssueModel;
import com.vranec.jpa.model.TimeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IssuesRepository extends JpaRepository<IssueModel, Long> {

    //@Query("SELECT i.issueId FROM IssueModel i where i.issueId=:issueId")
    List<IssueModel> findByIssueId(String issueId);
}

