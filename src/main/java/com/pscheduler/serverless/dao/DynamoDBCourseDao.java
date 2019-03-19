package com.pscheduler.serverless.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.pscheduler.serverless.manager.DynamoDBManager;
import com.pscheduler.serverless.pojo.Course;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        String search = queryString.replaceAll("\\W", "");
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
        eav.put(":filter", new AttributeValue().withS(filter));

        DynamoDBQueryExpression<Course> query = new DynamoDBQueryExpression<Course>()
            .withExpressionAttributeValues(eav)
            .withKeyConditionExpression("term = :term")
            .withFilterExpression("contains(searchName, :filter)");

        return mapper.query(Course.class, query);
    }

    @Override
    public List<Course> getCoursesForTerm(int term) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":term", new AttributeValue().withN("" + term));

        DynamoDBQueryExpression<Course> query = new DynamoDBQueryExpression<Course>()
            .withExpressionAttributeValues(eav)
            .withKeyConditionExpression("term = :term");

        return mapper.query(Course.class, query);
    }

    @Override
    public void deleteCourses(List<Course> courses) {
        mapper.batchDelete(courses);
    }

    @Override
    public void saveCourses(List<Course> courses) {
        mapper.batchSave(courses);
    }

    @Override
    public void deleteCoursesForTerm(int term) {
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":term", new AttributeValue().withN("" + term));
        DynamoDBQueryExpression<Course> query = new DynamoDBQueryExpression<Course>()
            .withKeyConditionExpression("term = :term")
            .withExpressionAttributeValues(eav);
        mapper.batchDelete(mapper.query(Course.class, query));
    }
}
