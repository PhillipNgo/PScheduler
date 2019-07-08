package com.pscheduler.util;

import com.pscheduler.server.model.CourseGPA;

public class GradeBuilder {

    private String subject;
    private String courseNumber;
    private String name;
    private String instructor;
    private int crn;
    private int credits;
    private double gpa;
    private int students;
    private double A;
    private double B;
    private double C;
    private double D;
    private double F;
    private int withdraws;
    private int term;

    public GradeBuilder() {
        this.reset();
    }

    public void reset() {
        this.subject = null;
        this.courseNumber = null;
        this.name = null;
        this.instructor = null;
        this.crn = 0;
        this.credits = 0;
        this.gpa = 0.0;
        this.students = 0;
        this.A = 0.0;
        this.B = 0.0;
        this.C = 0.0;
        this.D = 0.0;
        this.F = 0.0;
        this.withdraws = 0;
        this.term = 0;
    }

    public GradeBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    public GradeBuilder courseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
        return this;
    }

    public GradeBuilder name(String name) {
        this.name = name;
        return this;
    }

    public GradeBuilder instructor(String instructor) {
        this.instructor = instructor;
        return this;
    }

    public GradeBuilder crn(int crn) {
        this.crn = crn;
        return this;
    }

    public GradeBuilder credits(int credits) {
        this.credits = credits;
        return this;
    }

    public GradeBuilder gpa(double gpa) {
        this.gpa = gpa;
        return this;
    }

    public GradeBuilder students(int students) {
        this.students = students;
        return this;
    }

    public GradeBuilder A(double A) {
        this.A = A;
        return this;
    }

    public GradeBuilder B(double B) {
        this.B = B;
        return this;
    }

    public GradeBuilder C(double C) {
        this.C = C;
        return this;
    }

    public GradeBuilder D(double D) {
        this.D = D;
        return this;
    }

    public GradeBuilder F(double F) {
        this.F = F;
        return this;
    }

    public GradeBuilder withdraws(int withdraws) {
        this.withdraws = withdraws;
        return this;
    }

    public GradeBuilder term(int term) {
        this.term = term;
        return this;
    }

    public CourseGPA build() {
        return new CourseGPA(
              subject,
              courseNumber,
              name,
              instructor,
              crn,
              credits,
              gpa,
              students,
              A,
              B,
              C,
              D,
              F,
              withdraws,
              term
        );
    }
}
