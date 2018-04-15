package com.vranec.jpa.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Table(name = "Issues")
public class IssueModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @Column(name = "issue_id")
    private String issueId;

    @Getter
    @Column(name = "update_date")
    private Date updateDate;

    @Getter
    @Column(name = "assignee")
    private String assignee;

    @Getter
    @Column(name = "summary")
    private String summary;

    @Getter
    @Column(name = "status")
    private String status;

    public IssueModel() {
    }

    public IssueModel(Long id, String issueId, Date updateDate, String assignee, String summary, String status) {
        this.issueId = issueId;
        this.updateDate = updateDate;
        this.assignee = assignee;
        this.summary = summary;
        this.status = status;
    }

    @Override
    public String toString() {
        return "id: " + id + " issue: " + issueId;
    }

}

