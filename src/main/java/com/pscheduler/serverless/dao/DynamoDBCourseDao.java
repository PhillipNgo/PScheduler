package com.pscheduler.serverless.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.pscheduler.serverless.manager.DynamoDBManager;
import com.pscheduler.serverless.pojo.Course;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            .withKeyConditionExpression("term = :term");
        
        Matcher subjectCourseRegex = Pattern.compile("(.{1,5}?)(\\d{1,4}h?)").matcher(filter);
        if (subjectCourseRegex.matches()) {
            subjectCourseRegex.find();
            eav.put(":subject" , new AttributeValue().withS(subjectCourseRegex.group(0)));
            eav.put(":courseNumber" , new AttributeValue().withS(subjectCourseRegex.group(1)));
            query = query
                .withFilterExpression("subject = :subject")
                .withFilterExpression("begins_with(courseNumber, :courseNumber)");
        } else {
            eav.put(":name" , new AttributeValue().withS(filter));
            query = query.withFilterExpression("contains(searchName, :name)");
        }

        return mapper.query(Course.class, query.withExpressionAttributeValues(eav));
    }

    @Override
    public void saveCourses(List<Course> courses) {
        mapper.batchSave(courses);
    }
}
