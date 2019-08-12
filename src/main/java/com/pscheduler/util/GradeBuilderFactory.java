package com.pscheduler.util;

public class GradeBuilderFactory {

    private String factoryClassName;

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

    public GradeBuilderFactory(Class<? extends CourseGPA> factoryClass) {
        this.factoryClassName = factoryClass.getName();
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

    public GradeBuilderFactory subject(String subject) {
        this.subject = subject;
        return this;
    }

    public GradeBuilderFactory courseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
        return this;
    }

    public GradeBuilderFactory name(String name) {
        this.name = name;
        return this;
    }

    public GradeBuilderFactory instructor(String instructor) {
        this.instructor = instructor;
        return this;
    }

    public GradeBuilderFactory crn(int crn) {
        this.crn = crn;
        return this;
    }

    public GradeBuilderFactory credits(int credits) {
        this.credits = credits;
        return this;
    }

    public GradeBuilderFactory gpa(double gpa) {
        this.gpa = gpa;
        return this;
    }

    public GradeBuilderFactory students(int students) {
        this.students = students;
        return this;
    }

    public GradeBuilderFactory A(double A) {
        this.A = A;
        return this;
    }

    public GradeBuilderFactory B(double B) {
        this.B = B;
        return this;
    }

    public GradeBuilderFactory C(double C) {
        this.C = C;
        return this;
    }

    public GradeBuilderFactory D(double D) {
        this.D = D;
        return this;
    }

    public GradeBuilderFactory F(double F) {
        this.F = F;
        return this;
    }

    public GradeBuilderFactory withdraws(int withdraws) {
        this.withdraws = withdraws;
        return this;
    }

    public GradeBuilderFactory term(int term) {
        this.term = term;
        return this;
    }

    public CourseGPA build() {
        switch (this.factoryClassName) {
            case "com.pscheduler.server.model.CourseGPA":
                return new com.pscheduler.server.model.CourseGPA(
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
            case "com.pscheduler.serverless.pojo.CourseGPA":
                return new com.pscheduler.serverless.pojo.CourseGPA(
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
            default:
                return null;
        }
    }
}
