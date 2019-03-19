package com.pscheduler.serverless.dao;

import com.pscheduler.serverless.pojo.Course;

import java.util.List;

public interface CourseDao {
    List<Course> searchCourses(int term, String query);
    void saveCourses(List<Course> courses);
    void deleteCoursesForTerm(int term);
    List<Course> getCoursesForTerm(int term);
    public void deleteCourses(List<Course> courses);
}
