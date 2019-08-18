package com.pscheduler.serverless.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.pscheduler.serverless.manager.DynamoDBManager;
import com.pscheduler.serverless.pojo.CourseGPA;

import java.util.ArrayList;
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
        String[] splitQuery = queryString.split("-");
        if (splitQuery.length != 2) {
            return new ArrayList<>();
        }

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":subject", new AttributeValue().withS(splitQuery[0].toUpperCase()));
        eav.put(":courseNumber", new AttributeValue().withS(splitQuery[1]));
        eav.put(":term", new AttributeValue().withN("" + term));

        DynamoDBQueryExpression<CourseGPA> query = new DynamoDBQueryExpression<CourseGPA>()
            .withExpressionAttributeValues(eav)
            .withKeyConditionExpression("subject = :subject")
            .withFilterExpression("courseNumber = :courseNumber and term >= :term");

        return mapper.query(CourseGPA.class, query);
    }

    @Override
    public void saveCourseGPAs(List<CourseGPA> courses) {
        mapper.batchSave(courses);
    }

}
