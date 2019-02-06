package com.pscheduler.serverless.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.pscheduler.serverless.dao.DynamoDBCourseDao;
import com.pscheduler.serverless.pojo.Course;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CourseFunctions implements RequestHandler<CourseRequest, List<Course>> {

    private static final DynamoDBCourseDao courseDao = DynamoDBCourseDao.instance();

    @Override
    public List<Course> handleRequest(CourseRequest request, Context context) {
        if (request.term == 0 || request.query == null) {
            return new ArrayList<>();
        }
        return request.query.stream()
            .map(q -> q.length() < 2 ? new ArrayList<Course>() : courseDao.searchCourses(request.term, q))
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }
}
