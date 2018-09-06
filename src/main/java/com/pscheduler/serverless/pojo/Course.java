package com.pscheduler.serverless.pojo;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.util.ArrayList;
import java.util.List;

@DynamoDBTable(tableName = "Courses")
public class Course implements com.pscheduler.util.Course {

    private int term;
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

    public Course() {}

    public Course(
        int term,
        int crn,
        String subject,
        String courseNumber,
        String name,
        String type,
        int credits,
        int capacity,
        String instructor,
        List<com.pscheduler.util.Meeting> meetings,
        String exam
    ) {
        this.term = term;
        this.crn = crn;
        this.subject = subject;
        this.courseNumber = courseNumber;
        this.name = name;
        this.type = type;
        this.credits = credits;
        this.capacity = capacity;
        this.instructor = instructor;
        this.meetings = new ArrayList<>();
        for (com.pscheduler.util.Meeting meeting : meetings) {
            this.meetings.add((Meeting) meeting);
        }
        this.exam = exam;
    }

    @DynamoDBHashKey(attributeName = "term")
    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    @DynamoDBRangeKey(attributeName = "crn")
    public int getCrn() {
        return crn;
    }

    public void setCrn(int crn) {
        this.crn = crn;
    }

    @DynamoDBAttribute(attributeName = "subject")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @DynamoDBAttribute(attributeName = "courseNumber")
    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @DynamoDBAttribute(attributeName = "credits")
    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    @DynamoDBAttribute(attributeName = "capacity")
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @DynamoDBAttribute(attributeName = "instructor")
    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    @DynamoDBAttribute(attributeName = "meetings")
    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }

    @DynamoDBAttribute(attributeName = "exam")
    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    @DynamoDBDocument
    public static class Meeting implements com.pscheduler.util.Meeting {

        private String location;
        private String startTime;
        private String endTime;
        private List<String> days;

        public Meeting() {}

        public Meeting(String location, String startTime, String endTime, List<String> days) {
            this.location = location;
            this.startTime = startTime;
            this.endTime = endTime;
            this.days = days;
        }

        @DynamoDBAttribute(attributeName = "location")
        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        @DynamoDBAttribute(attributeName = "startTime")
        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        @DynamoDBAttribute(attributeName = "endTime")
        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        @DynamoDBAttribute(attributeName = "days")
        public List<String> getDays() {
            return days;
        }

        public void setDays(List<String> days) {
            this.days = days;
        }
    }
}
