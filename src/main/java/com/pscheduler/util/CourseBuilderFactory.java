package com.pscheduler.util;

import java.util.List;
import java.util.ArrayList;

public class CourseBuilderFactory {
    
    private String factoryClassName;

    private int crn;
    private String subject;
    private String courseNumber;
    private String name;
    private String type;
    private int credits;
    private int capacity;
    private String instructor;
    private List<Meeting> meetings;
    private String exam;
    private int term;

    public CourseBuilderFactory(Class<?> factoryClass) {
        this.factoryClassName = factoryClass.getName();
        this.reset();
    }

    public void reset() {
        this.crn = 0;
        this.subject = null;
        this.courseNumber = null;
        this.name = null;
        this.type = null;
        this.credits = 0;
        this.capacity = 0;
        this.instructor = null;
        this.meetings = new ArrayList<>();
        this.exam = "00X";
        this.term = 0;
    }

    public CourseBuilderFactory crn(int crn) {
        this.crn = crn;
        return this;
    }
    
    public CourseBuilderFactory subject(String subject) {
        this.subject = subject;
        return this;
    }

    public CourseBuilderFactory courseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
        return this;
    }

    public CourseBuilderFactory name(String name) {
        this.name = name;
        return this;
    }

    public CourseBuilderFactory type(String type) {
        this.type = type;
        return this;
    }

    public CourseBuilderFactory credits(int credits) {
        this.credits = credits;
        return this;
    }

    public CourseBuilderFactory capacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public CourseBuilderFactory instructor(String instructor) {
        this.instructor = instructor;
        return this;
    }

    public CourseBuilderFactory meeting(Meeting meeting) {
        if (meeting != null) {
            this.meetings.add(meeting);
        }
        return this;
    }

    public CourseBuilderFactory meeting(String location, String startTime, String endTime, List<String> days) {
        return this.meeting(this.buildMeeting(location, startTime, endTime, days));
    }

    public CourseBuilderFactory meetings(List<Meeting> meetings) {
        for (Meeting meeting : meetings) {
            this.meetings.add(meeting);
        }
        return this;
    }

    public CourseBuilderFactory exam(String exam) {
        this.exam = exam;
        return this;
    }

    public CourseBuilderFactory term(int term) {
        this.term = term;
        return this;
    }

    public Meeting buildMeeting(String location, String startTime, String endTime, List<String> days) {
        switch (this.factoryClassName) {
            case "com.pscheduler.server.model.Course":
                return new com.pscheduler.server.model.Meeting(
                    location,
                    startTime,
                    endTime,
                    days
                );
            case "com.pscheduler.serverless.pojo.Course":
                return new com.pscheduler.serverless.pojo.Course.Meeting(
                    location,
                    startTime,
                    endTime,
                    days
                );
            default:
                return null;
        }
    }

    public Course build() {
        switch (this.factoryClassName) {
            case "com.pscheduler.server.model.Course":
                return new com.pscheduler.server.model.Course(
                    term,
                    crn,
                    subject,
                    courseNumber,
                    name,
                    type,
                    credits,
                    capacity,
                    instructor,
                    meetings,
                    exam
                );
            case "com.pscheduler.serverless.pojo.Course":
                return new com.pscheduler.serverless.pojo.Course(
                    term,
                    crn,
                    subject,
                    courseNumber,
                    name,
                    type,
                    credits,
                    capacity,
                    instructor,
                    meetings,
                    exam
                );
            default:
                return null;
        }
    }
}