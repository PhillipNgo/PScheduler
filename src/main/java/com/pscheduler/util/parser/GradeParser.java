package com.pscheduler.util.parser;


import com.pscheduler.server.model.CourseGPA;
import com.pscheduler.util.GradeBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class responsible for reading data from the Grade Distribution Files and parsing them
 *
 * @author Francis Nguyen
 */
public class GradeParser {

    private GradeBuilder gradeBuilder;

    // delimeter for splitting the line of data in the GPA Files
    private final String SPLIT_DELIMETER = ",";

    /**
     * Default Constructor
     */
    public GradeParser() {
        this.gradeBuilder = new GradeBuilder();
    }

    /**
     * Gets the term from a given file name
     *
     * @param term the name of the file
     * @return the term ID
     */
    public int getTerm(String term) {
        String semester = term.substring(0, term.length() - 4);
        int month = 0;

        switch (semester) {
            case "Spring":
                month = 1;
                break;
            case "SummerI":
                month = 6;
                break;
            case "SummerII":
                month = 7;
                break;
            case "Fall":
                month = 9;
                break;
            case "Winter":
                month = 12;
                break;
            default:
                throw new IllegalArgumentException("Not a valid semester");
        }

        int year = Integer.parseInt(term.substring(term.length() - 4));
        return year * 100 + month;
    }

    /**
     * Creates a CourseGPA from a string that has been extracted a GPA Data File
     *
     * @param line the string to be parsed
     * @param term name of file being parsed containing the term
     * @return the parsed course
     */
    private CourseGPA makeClass(String line, String term) {
        int termID = getTerm(term);

        gradeBuilder.reset();
        String[] values = line.split(SPLIT_DELIMETER);

        if (values.length == 1) {
            return null;
        }

        gradeBuilder
                .subject(values[0])
                .courseNumber(values[1])
                .name(values[2])
                .instructor(values[3])
                .crn(Integer.parseInt(values[4]))
                .credits(Integer.parseInt(values[5]))
                .gpa(Double.parseDouble(values[6]))
                .students(Integer.parseInt(values[7]))
                .A(Double.parseDouble(values[8]))
                .B(Double.parseDouble(values[9]))
                .C(Double.parseDouble(values[10]))
                .D(Double.parseDouble(values[11]))
                .F(Double.parseDouble(values[12]))
                .withdraws(Integer.parseInt(values[13]))
                .term(termID);

        return gradeBuilder.build();
    }

    /**
     * Parses a term file containing GPA data
     *
     * @param path path to term file
     * @return list containing all the parsed gpa data
     * @throws Exception
     */
    public List<CourseGPA> parseTermFile(String path) throws Exception {
        List<CourseGPA> courses = new ArrayList<>();
        File file = new File(path);
        Scanner scanner = new Scanner(file);

        // Need to obtain the file name to determine term
        String[] split = path.split("/");
        String fileName = split[split.length - 1];
        String term = fileName.substring(0, fileName.length() - 4);

        scanner.nextLine(); // Skip the first row which contain headers
        while (scanner.hasNextLine()) {
            CourseGPA grade = makeClass(scanner.nextLine(), term);
            if (grade != null) courses.add(grade);
        }
        scanner.close();
        return courses;
    }

    /**
     * Parses every term file inside a folder
     *
     * @param path path to folder containing term year (which contains term files)
     * @return list holding the gpa data for each class
     * @throws Exception
     */
    public List<CourseGPA> parseAllFiles(String path) throws Exception {
        List<CourseGPA> courses = new ArrayList<>();
        File folder = new File(path);
        File[] files = folder.listFiles();

        for (File file : files ) {
            if (file.isDirectory()) {
                courses.addAll(parseAllFiles(path + "/" + file.getName()));
            } else if(file.isFile()) {
                courses.addAll(parseTermFile(path + "/" + file.getName()));
            }
        }

        return courses;
    }

}
