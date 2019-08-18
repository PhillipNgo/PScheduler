package com.pscheduler.serverless.pojo;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

@DynamoDBTable(tableName = "CourseGPAs")
public class CourseGPA implements com.pscheduler.util.CourseGPA {

    private int crn;
    private String subject;
    private String courseNumber;
    private String name;
    private String instructor;
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
    private String searchName;

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
        this.searchName = (subject + courseNumber + " " + name.replaceAll("\\W", "")).toLowerCase();
    }

    @DynamoDBAttribute(attributeName = "term")
    public int getTerm() {
    	return this.term;
    }

    public void setTerm(int term) {
    	this.term = term;
    }

    @DynamoDBRangeKey(attributeName = "crn")
    public int getCrn() {
        return this.crn;
    }

    public void setCrn(int crn) {
        this.crn = crn;
    }

    @DynamoDBHashKey(attributeName = "subject")
    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @DynamoDBAttribute(attributeName = "courseNumber")
    public String getCourseNumber() {
        return this.courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "instructor")
    public String getInstructor() {
        return this.instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    @DynamoDBAttribute(attributeName = "credits")
    public int getCredits() {
        return this.credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    @DynamoDBAttribute(attributeName = "gpa")
    public double getGpa() {
        return this.gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    @DynamoDBAttribute(attributeName = "students")
    public int getStudents() {
        return this.students;
    }

    public void setStudents(int students) {
        this.students = students;
    }

    @DynamoDBAttribute(attributeName = "A")
    public double getA() {
        return this.A;
    }

    public void setA(double A) {
        this.A = A;
    }

    @DynamoDBAttribute(attributeName = "B")
    public double getB() {
        return this.B;
    }

    public void setB(double B) {
        this.B = B;
    }

    @DynamoDBAttribute(attributeName = "C")
    public double getC() {
        return this.C;
    }

    public void setC(double C) {
        this.C = C;
    }

    @DynamoDBAttribute(attributeName = "D")
    public double getD() {
        return this.D;
    }

    public void setD(double D) {
        this.D = D;
    }

    @DynamoDBAttribute(attributeName = "F")
    public double getF() {
        return this.F;
    }

    public void setF(double F) {
        this.F = F;
    }

    @DynamoDBAttribute(attributeName = "withdraws")
    public int getWithdraws() {
        return this.withdraws;
    }

    public void setWithdraws(int withdraws) {
        this.withdraws = withdraws;
    }

    @DynamoDBAttribute(attributeName = "searchName")
    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

}
