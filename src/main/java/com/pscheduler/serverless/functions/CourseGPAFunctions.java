package com.pscheduler.serverless.functions;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.pscheduler.serverless.dao.DynamoDBCourseGPADao;
import com.pscheduler.serverless.pojo.CourseGPA;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CourseGPAFunctions implements RequestHandler<CourseRequest, List<CourseGPA>> {

    private static final DynamoDBCourseGPADao courseGPADao = DynamoDBCourseGPADao.instance();

    @Override
    public List<CourseGPA> handleRequest(CourseRequest request, Context context) {
        if (request.term == 0 || request.query == null) {
            return new ArrayList<>();
        }
        return request.query.stream()
            .map(q -> q.length() < 2 ? new ArrayList<CourseGPA>() : courseGPADao.searchCourseGPAs(request.term, q))
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    }
}
