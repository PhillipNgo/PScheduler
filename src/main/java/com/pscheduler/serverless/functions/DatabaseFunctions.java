package com.pscheduler.serverless.functions;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.pscheduler.serverless.dao.DynamoDBCourseDao;
import com.pscheduler.serverless.pojo.Course;
import com.pscheduler.util.parser.VTParser;
import org.apache.log4j.Logger;

public class DatabaseFunctions implements RequestHandler<CourseRequest, Void> {

    private static final Logger log = Logger.getLogger(DatabaseFunctions.class);

    private static final DynamoDBCourseDao courseDao = DynamoDBCourseDao.instance();


    @Override
    @SuppressWarnings("unchecked")
    public Void handleRequest(CourseRequest request, Context context) {
        try {
            VTParser parser = new VTParser(Course.class, request.getTerm());
            List<com.pscheduler.util.Course> courseListGeneric;
            if (request.getQuery().equals("file")) {
                courseListGeneric = parser.parseTermFile(request.getTerm() + ".txt");
            } else {
                courseListGeneric = parser.parseTerm();
            }
            courseDao.saveCourses((List<Course>) (List<?>) courseListGeneric);
        } catch (Exception e) {
            log.error("Failed to update term courses", e);
        }
        return null;
    }
}
