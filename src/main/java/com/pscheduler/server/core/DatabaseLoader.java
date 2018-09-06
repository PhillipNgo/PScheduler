package com.pscheduler.server.core;

import com.pscheduler.server.model.Course;
import com.pscheduler.server.model.Meeting;
import com.pscheduler.server.repository.CourseRepository;
import com.pscheduler.server.repository.MeetingRepository;
import com.pscheduler.util.parser.VTParser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseLoader implements ApplicationRunner {
    private final CourseRepository courses;
    private final MeetingRepository meetings;
    private final int TERM = 201809;

    @Autowired
    public DatabaseLoader (CourseRepository courses, MeetingRepository meetings) {
        this.courses = courses;
        this.meetings = meetings;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        VTParser parser = new VTParser(Course.class, TERM);

        List<com.pscheduler.util.Course> courseListGeneric = parser.parseTermFile("./src/main/resources/data/" + TERM + ".txt");
        List<Course> courseList = new ArrayList<>();
        for (com.pscheduler.util.Course course : courseListGeneric) courseList.add((Course) course);
        for (Course course : courseList) {
            for (com.pscheduler.util.Meeting meeting : course.getMeetings()) {
                Meeting serverMeeting = (Meeting) meeting;
                meetings.save(serverMeeting);
            }
        }
        courses.save(courseList);
    }
}
