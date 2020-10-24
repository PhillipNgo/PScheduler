package com.pscheduler.serverless.manager;

import java.util.List;
import java.util.stream.Collectors;

import com.pscheduler.serverless.dao.DynamoDBCourseDao;
import com.pscheduler.serverless.pojo.Course;
import com.pscheduler.util.parser.VTParser;

public class DynamoDBCourseUpdater {

    private static final DynamoDBCourseDao courseDao = DynamoDBCourseDao.instance();

    public static void main(String[] args) throws Exception {
        int[] terms = new int[]{202009};
        for (int term : terms) {
            updateTerm(term);
            // deleteTerm(term);
        }
    }

    @SuppressWarnings("unchecked")
    private static void updateTerm(int term) throws Exception {

        // List of courses to update
        VTParser parser = new VTParser(Course.class, term);
        List<Course> courseListGeneric = (List<Course>) (List<?>) parser.parseTermFile("./src/main/resources/data/courses/" + term + ".txt");

        // List of courses currently in DB
        List<Course> current = courseDao.getCoursesForTerm(term);

        // Remove courses from DB that were removed from the Timetable
        List<Course> filtered = current
                .stream()
                .filter(c1 -> courseListGeneric.stream().noneMatch(c2 -> c1.getCrn() == c2.getCrn()))
                .collect(Collectors.toList());
        courseDao.deleteCourses(filtered);

        // Add / Update remaining courses
        courseDao.saveCourses(courseListGeneric);
    }

    private static void deleteTerm(int term) {
        courseDao.deleteCoursesForTerm(term);
    }

}
