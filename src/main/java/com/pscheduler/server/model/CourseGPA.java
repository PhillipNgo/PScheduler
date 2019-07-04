package com.pscheduler.server.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class CourseGPA {

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
    @Size(min = 1, max = 50)
    private String instructor;

    @Min(value = 0)
    @Max(value = 50)
    private int credits;

    @NotNull
    @Min(value = 0)
    @Max(value = 4)
    private double gpa;

    @NotNull
    @Min(value = 0)
    @Max(value = 10000)
    private int students;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    private double A;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    private double B;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    private double C;

    @Min(value = 0)
    @Max(value = 100)
    private double D;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    private double F;

    @NotNull
    @Min(value = 0)
    @Max(value = 1000)
    private int withdraws;

    @NotNull
    @Min(value = 100000)
    @Max(value = 999999)
    private int term;

    public CourseGPA() {}

    public CourseGPA(
        String subject,
        String courseNumber,
        String name,
        String instructor,
        int crn,
        int credits,
        double gpa,
        int students,
        double A,
        double B,
        double C,
        double D,
        double F,
        int withdraws,
        int term
    ) {
        this.subject = subject;
        this.courseNumber = courseNumber;
        this.name = name;
        this.instructor = instructor;
        this.crn = crn;
        this.credits = credits;
        this.gpa = gpa;
        this.students = students;
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.F = F;
        this.withdraws = withdraws;
        this.term = term;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCourseNumber() {
        return this.courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructor() {
        return this.instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public int getCrn() {
        return this.crn;
    }

    public void setCrn(int crn) {
        this.crn = crn;
    }

    public int getCredits() {
        return this.credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public double getGpa() {
        return this.gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public int getStudents() {
        return this.students;
    }

    public void setStudents(int students) {
        this.students = students;
    }

    public double getA() {
        return this.A;
    }

    public void setA(double A) {
        this.A = A;
    }

    public double getB() {
        return this.B;
    }

    public void setB(double B) {
        this.B = B;
    }

    public double getC() {
        return this.C;
    }

    public void setC(double C) {
        this.C = C;
    }

    public double getD() {
        return this.D;
    }

    public void setD(double D) {
        this.D = D;
    }

    public double getF() {
        return this.F;
    }

    public void setF(double F) {
        this.F = F;
    }

    public int getWithdraws() {
        return this.withdraws;
    }

    public void setWithdraws(int withdraws) {
        this.withdraws = withdraws;
    }

    public int getTerm() { return this.term; }

    public void setTerm(int term) { this.term = term; }
}
