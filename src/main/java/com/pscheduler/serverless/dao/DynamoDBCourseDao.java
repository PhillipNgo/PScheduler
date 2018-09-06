package com.pscheduler.serverless.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.pscheduler.serverless.manager.DynamoDBManager;
import com.pscheduler.serverless.pojo.Course;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DynamoDBCourseDao implements CourseDao {

    private static final DynamoDBMapper mapper = DynamoDBManager.mapper();

    private static volatile DynamoDBCourseDao instance;

    private DynamoDBCourseDao() {}

    public static DynamoDBCourseDao instance() {

        if (instance == null) {
            synchronized(DynamoDBCourseDao.class) {
                if (instance == null) {
                    instance = new DynamoDBCourseDao();
                }
            }
        }
        return instance;
    }

    @Override
    public List<Course> searchCourses(int term, String queryString) {

        String search = queryString.trim().replaceAll(" ", "");
        try {
            Course searchCrn = mapper.load(Course.class, term, Integer.parseInt(queryString));
            if (searchCrn != null) {
                search = searchCrn.getSubject() + searchCrn.getCourseNumber();
            }
        } catch (NumberFormatException e) {
            // queryString not a valid number. Ok to move on
        }
        final String filter = search.toLowerCase();

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":term", new AttributeValue().withN("" + term));

        DynamoDBQueryExpression<Course> query = new DynamoDBQueryExpression<Course>()
            .withExpressionAttributeValues(eav)
            .withKeyConditionExpression("term = :term");

        return mapper.query(Course.class, query)
                .stream()
                .filter(course -> 
                    (course.getSubject() + course.getCourseNumber()).toLowerCase().contains(filter)
                    || course.getName().replaceAll(" ", "").toLowerCase().contains(filter)
                )
                .collect(Collectors.toList());
    }

    @Override
    public void saveCourses(List<Course> courses) {
        mapper.batchSave(courses);
    }
}
