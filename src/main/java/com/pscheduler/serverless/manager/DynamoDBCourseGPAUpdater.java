package com.pscheduler.serverless.manager;

import java.util.List;

import com.pscheduler.serverless.dao.DynamoDBCourseGPADao;
import com.pscheduler.serverless.pojo.CourseGPA;
import com.pscheduler.util.parser.GradeParser;

public class DynamoDBCourseGPAUpdater {

    private static final DynamoDBCourseGPADao courseGPADao = DynamoDBCourseGPADao.instance();

    public static void main(String[] args) throws Exception {
        int[] terms = new int[]{201901};
        for (int term : terms) {
            addTerm(term);
        }
    }

    @SuppressWarnings("unchecked")
    private static void addTerm(int term) throws Exception {
        GradeParser parser = new GradeParser(CourseGPA.class);
        List<CourseGPA> courseGPAList = (List<CourseGPA>) (List<?>) parser.parseAllFiles();
        courseGPADao.saveCourseGPAs(courseGPAList);
    }
}
