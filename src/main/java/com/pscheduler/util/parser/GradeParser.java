package com.pscheduler.util.parser;


import com.pscheduler.server.model.CourseGPA;
import com.pscheduler.util.GradeBuilderFactory;

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

    private GradeBuilderFactory gradeBuilderFactory;

    // delimeter for splitting the line of data in the GPA Files
    private final String SPLIT_DELIMETER = ",";

    /**
     * Default Constructor
     */
    public GradeParser() {
        this.gradeBuilderFactory = new GradeBuilderFactory();
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

        if (semester.equals("Spring")) {
            month = 1;
        } else if (semester.equals("SummerI")) {
            month = 6;
        } else if (semester.equals("SummerII")) {
            month = 7;
        } else if (semester.equals("Fall")) {
            month = 9;
        } else if (semester.equals("Winter")) {
            month = 12;
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

        gradeBuilderFactory.reset();
        String[] values = line.split(SPLIT_DELIMETER);

        if (values.length != 14) {
            System.out.println("bad line formatting: " + line);
            return null;
        }

        gradeBuilderFactory
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

        return gradeBuilderFactory.build();
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

        for (int x = 0; x < files.length; x++) {
            if (files[x].isDirectory()) {
                courses.addAll(parseAllFiles(path + "/" + files[x].getName()));
            } else if(files[x].isFile()) {
                courses.addAll(parseTermFile(path + "/" + files[x].getName()));
            }
        }

        return courses;
    }

}
