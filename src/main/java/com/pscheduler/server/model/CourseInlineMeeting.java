package com.pscheduler.server.model;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

@Projection(name="courseInlineMeeting", types = { Course.class })
public interface CourseInlineMeeting {
    public int getTerm();
    public int getCrn();
    public String getSubject();
    public String getCourseNumber();
    public String getName();
    public String getType();
    public int getCredits();
    public int getCapacity();
    public String getInstructor();
    List<Meeting> getMeetings();
    public String getExam();
}
