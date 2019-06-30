package com.pscheduler.server.model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Course implements com.pscheduler.util.Course {
    @Id
    @Min(value = 10000)
    @Max(value = 99999)
    private int crn;

    @NotNull
    @Size(min = 2, max = 6)
    private String subject;

    @NotNull
    @Size(min = 4, max = 5)
    private String courseNumber;

    @NotNull
    @Size(min = 1, max = 100)
    private String name;

    @NotNull
    @Size(min = 1, max = 10)
    private String type;

    @Min(value = 0)
    @Max(value = 50)
    private int credits;

    @Min(value = 0)
    @Max(value = 100000)
    private int capacity;

    @NotNull
    @Size(min = 1, max = 50)
    private String instructor;

    @OneToMany
    private List<Meeting> meetings;

    @NotNull
    @Size(min = 1, max = 5)
    private String exam;

    @NotNull
    @Min(value = 100000)
    @Max(value = 999999)
    private int term;

    @Version
    private long version;

    public Course() {
        meetings = new ArrayList<>();
    }

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
        this.term = term;
    }

    public int getCrn() {
        return crn;
    }

    public String getSubject() {
        return subject;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getCredits() {
        return credits;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getInstructor() {
        return instructor;
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void addMeeting(Meeting meeting) {
        meetings.add(meeting);
    }

    public String getExam() {
        return exam;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }
}
