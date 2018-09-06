package com.pscheduler.serverless.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.pscheduler.serverless.dao.DynamoDBCourseDao;
import com.pscheduler.serverless.pojo.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseFunctions implements RequestHandler<CourseRequest, List<Course>> {

    private static final DynamoDBCourseDao courseDao = DynamoDBCourseDao.instance();

    @Override
    public List<Course> handleRequest(CourseRequest request, Context context) {
        if (request.term == 0 || request.query == null || request.query.length() < 2) {
            return new ArrayList<>();
        }
        return courseDao.searchCourses(request.term, request.query);
    }
}
