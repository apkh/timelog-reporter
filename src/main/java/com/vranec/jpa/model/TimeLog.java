package com.vranec.jpa.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import javax.persistence.*;

@Builder
@Entity
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
    @Column(name="date")
    private Date date;

    public TimeLog() {
    }

    public TimeLog(Long id, int reportTime, Date reportDate) {
        this.reportTime = reportTime;
        this.date = reportDate;
    }

    @Override
    public String toString() {
        return "id: " + id + " time: " + reportTime;
    }
}