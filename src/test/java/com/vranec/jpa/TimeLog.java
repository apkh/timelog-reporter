package com.vranec.jpa;

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
    private Date reporTime;

    public TimeLog() {
    }

    public TimeLog(Long id, Date reporTime) {
        this.reporTime = reporTime;
    }

    @Override
    public String toString() {
        return "id: " + id + " time: " + reporTime;
    }
}