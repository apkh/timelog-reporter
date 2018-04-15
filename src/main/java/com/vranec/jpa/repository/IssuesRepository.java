package com.vranec.jpa.repository;

import com.vranec.jpa.model.IssueModel;
import com.vranec.jpa.model.TimeLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuesRepository extends JpaRepository<IssueModel, Long> {

}

