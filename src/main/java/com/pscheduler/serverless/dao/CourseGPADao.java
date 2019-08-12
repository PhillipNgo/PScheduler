package com.pscheduler.serverless.dao;

import com.pscheduler.serverless.pojo.CourseGPA;

import java.util.List;

public interface CourseGPADao {
    List<CourseGPA> searchCourseGPAs(int term, String query);
    void saveCourseGPAs(List<CourseGPA> courseGPAs);
}
