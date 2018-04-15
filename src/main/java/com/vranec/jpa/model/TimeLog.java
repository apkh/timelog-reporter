package com.vranec.jpa.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import java.util.Date;
import javax.persistence.*;

@Builder
@Entity
@NamedQuery(name = "TimeLog.allDays", query = "SELECT DISTINCT tl.day FROM TimeLog tl ORDER BY tl.day")
@Table(name = "TimeLog")
public class TimeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="report_time")
    private int reportTime;

    @Column(name="report_date")
    private Date date;

    @Column(name="resource")
    private String resource;

    @ManyToOne
    private IssueModel issue;

    @Column(name="month_day")
    private int day;

    public TimeLog() {
    }

    @Builder
    public TimeLog(Long id, int reportTime, Date reportDate, String resource, IssueModel issue, int unused) {
        this.reportTime = reportTime;
        this.date = reportDate;
        this.day = new DateTime(date).getDayOfMonth();
        this.resource = resource;
        this.issue = issue;
    }

    @Override
    public String toString() {
        return "id: " + id + " time: " + reportTime;
    }
}