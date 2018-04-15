package com.vranec.jpa.model;

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

    @Getter
    @Setter
    @Column(name="report_time")
    private int reportTime;

    @Getter
    @Setter
    @Column(name="report_date")
    private Date date;

    @Getter
    @Setter
    @Column(name="month_day")
    private int day;

    @Getter
    @Setter
    @Column(name="resource")
    private String resource;

    public TimeLog() {
    }

    public TimeLog(Long id, int reportTime, Date reportDate, int unused, String resource) {
        this.reportTime = reportTime;
        this.date = reportDate;
        this.day = new DateTime(date).getDayOfMonth();
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "id: " + id + " time: " + reportTime;
    }
}