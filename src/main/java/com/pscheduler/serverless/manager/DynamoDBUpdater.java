package com.pscheduler.serverless.manager;

import java.util.List;

import com.pscheduler.serverless.dao.DynamoDBCourseDao;
import com.pscheduler.serverless.pojo.Course;
import com.pscheduler.util.parser.VTParser;

public class DynamoDBUpdater {

    private static final DynamoDBCourseDao courseDao = DynamoDBCourseDao.instance();

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        int[] terms = new int[]{201701, 201709, 201801, 201809};
        for (int term : terms) {
            VTParser parser = new VTParser(Course.class, term);
            List<com.pscheduler.util.Course> courseListGeneric;
            courseListGeneric = parser.parseTermFile("./src/main/resources/data/" + term + ".txt");
            // courseListGeneric = parser.parseTerm();
            courseDao.saveCourses((List<Course>) (List<?>) courseListGeneric);
        }
    }
}
