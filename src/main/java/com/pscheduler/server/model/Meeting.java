package com.pscheduler.server.model;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Meeting implements com.pscheduler.util.Meeting {

    @Id
    @GeneratedValue
    private int id;

    private String location;
    private String startTime;
    private String endTime;

    @ElementCollection
    private List<String> days;

    public Meeting() {}

    public Meeting(
        String location,
        String startTime, String endTime,
        List<String> days
    ) {
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.days = days;
    }

    public String getLocation() {
        return location;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public List<String> getDays() {
        return days;
    }
}
