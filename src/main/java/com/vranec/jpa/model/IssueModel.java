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
    @Setter
    @Column(name = "issue_id")
    private String issueId;

    @Getter
    @Setter
    @Column(name = "update_date")
    private Date updateDate;

    @Getter
    @Setter
    @Column(name = "assignee")
    private String assignee;

    public IssueModel() {
    }

    public IssueModel(Long id, String issueId, Date updateDate, String assignee) {
        this.issueId = issueId;
        this.updateDate = updateDate;
        this.assignee = assignee;
    }

    @Override
    public String toString() {
        return "id: " + id + " issue: " + issueId;
    }

}

