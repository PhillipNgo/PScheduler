package com.pscheduler.util.parser;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import com.pscheduler.server.model.Course;
import com.pscheduler.server.model.Meeting;

public class CourseFileCreator {
    
    public static void main(String[] args) throws Exception {
        outputTermDataFile(args[0]);
    }

    /**
     * Outputs class data and search options into an output file
     *
     * @param termYear the term value
     * @throws Exception
     */
    public static void outputTermDataFile(String termYear) throws Exception {
        VTParser parser = new VTParser(Course.class, Integer.parseInt(termYear));
        List<com.pscheduler.util.Course> list = parser.parseTerm();

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("src/main/resources/data/" + termYear + ".txt"), "utf-8"))) {
            for (com.pscheduler.util.Course genericCourse : list) {
                Course course = (Course) genericCourse;
                writer.write(course.getCrn() + "\t" + course.getSubject() + "-" + course.getCourseNumber() + "\t" +
                        course.getName() + "\t" + course.getType() + "\t" + course.getCredits() + "\t" +
                        course.getCapacity() + "\t" + course.getInstructor());
                List<Meeting> meetings = course.getMeetings();
                for (int i = 0; i < meetings.size(); i++) {
                    Meeting meeting = course.getMeetings().get(i);

                    writer.write("\t");

                    writer.write(meeting.getDays().size() == 0 ? "ARR" : String.join(" ", meeting.getDays()));

                    writer.write("\t");

                    if (meeting.getStartTime().contains("ARR") || meeting.getEndTime().contains("ARR")) {
                        writer.write("ARR");
                    } else {
                        writer.write(meeting.getStartTime());

                        writer.write("\t");

                        writer.write(meeting.getEndTime());
                    }

                    writer.write("\t");

                    writer.write(meeting.getLocation());

                    if (i == 0) {
                        writer.write("\t" + course.getExam());
                    }

                    if (i < meetings.size() - 1) {
                        writer.write("\tfill");
                    }
                }
                writer.write("\r\n");
            }
        }
    }
}
