package com.pscheduler.serverless.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.pscheduler.serverless.manager.DynamoDBManager;
import com.pscheduler.serverless.pojo.CourseGPA;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamoDBCourseGPADao implements CourseGPADao {

    private static final DynamoDBMapper mapper = DynamoDBManager.mapper();

    private static volatile DynamoDBCourseGPADao instance;

    private DynamoDBCourseGPADao() {}

    public static DynamoDBCourseGPADao instance() {

        if (instance == null) {
            synchronized(DynamoDBCourseGPADao.class) {
                if (instance == null) {
                    instance = new DynamoDBCourseGPADao();
                }
            }
        }
        return instance;
    }

    @Override
    public List<CourseGPA> searchCourseGPAs(int term, String queryString) {

        String search = queryString.replaceAll("\\W", "");
        try {
            CourseGPA searchCrn = mapper.load(CourseGPA.class, term, Integer.parseInt(queryString));
            if (searchCrn != null) {
                search = searchCrn.getSubject() + searchCrn.getCourseNumber();
            }
        } catch (NumberFormatException e) {
            // queryString not a valid number. Ok to move on
        }
        final String filter = search.toLowerCase();

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":term", new AttributeValue().withN("" + term));
        eav.put(":filter", new AttributeValue().withS(filter));

        DynamoDBQueryExpression<CourseGPA> query = new DynamoDBQueryExpression<CourseGPA>()
            .withExpressionAttributeValues(eav)
            .withKeyConditionExpression("term >= :term")
            .withFilterExpression("contains(searchName, :filter)");

        return mapper.query(CourseGPA.class, query);
    }

    @Override
    public void saveCourseGPAs(List<CourseGPA> courses) {
        mapper.batchSave(courses);
    }
}
