package com.pscheduler.util.parser;

import com.pscheduler.util.CourseGPA;
import com.pscheduler.util.GradeBuilderFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class responsible for reading data from the Grade Distribution Files and parsing them
 */
public class GradeParser {

    private GradeBuilderFactory gradeBuilderFactory;

    // Delimeter for splitting the line of data in the GPA Files
    private final String SPLIT_DELIMETER = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

    // Relative path to outputted grades data
    private final String GRADES_PATH = "src/main/resources/data/grades";

    /**
     * Default Constructor
     */
    public GradeParser(Class<? extends CourseGPA> courseType) {
        this.gradeBuilderFactory = new GradeBuilderFactory(courseType);
    }

    /**
     * Creates a CourseGPA from a string that has been extracted a GPA Data File
     *
     * @param line the string to be parsed
     * @param term name of file being parsed containing the term
     * @return the parsed course
     */
    private CourseGPA makeClass(String line, int term) {
        gradeBuilderFactory.reset();
        String[] values = line.split(SPLIT_DELIMETER);
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].trim();
        }

        if (values.length == 1) {
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
            .term(term);

        return gradeBuilderFactory.build();
    }

    /**
     * Parses a term file containing GPA data
     *
     * @param path path to term file
     * @return list containing all the parsed gpa data
     * @throws Exception
     */
    public List<CourseGPA> parseTermFile(int term) throws Exception {
        List<CourseGPA> courses = new ArrayList<>();
        Scanner scanner = new Scanner(new File(GRADES_PATH + "/" + term + ".csv"));

        while (scanner.hasNextLine()) {
            CourseGPA grade = makeClass(scanner.nextLine(), term);
            courses.add(grade);
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
    public List<CourseGPA> parseAllFiles() throws Exception {
        List<CourseGPA> courses = new ArrayList<>();
        File folder = new File(GRADES_PATH);
        File[] files = folder.listFiles();

        for (File file : files) {
            courses.addAll(parseTermFile(Integer.parseInt(file.getName().split("\\.")[0])));
        }

        return courses;
    }

    /**
     * Gets the term from a given file name
     * The file name must match the pattern <semester_type><year>.csv
     *
     * @param term the name of the file
     * @return the term ID
     */
    public int getTerm(String term) {
        String semester = term.substring(0, term.length() - 4);
        int year = Integer.parseInt(term.substring(term.length() - 4));
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
                year--;
                month = 12;
                break;
            default:
                throw new IllegalArgumentException("Not a valid semester");
        }

        return year * 100 + month;
    }

    /**
     * Parses a term csv, downloaded from the VT Grade Distribution
     * The file name must match the pattern <semester_type><year>.csv
     * 
     * @param path path to term file
     * @return list containing all the parsed gpa data
     * @throws Exception
     */
    public List<CourseGPA> parseOfficialTermFile(String path) throws Exception {
        List<CourseGPA> courses = new ArrayList<>();
        File file = new File(path);
        Scanner scanner = new Scanner(file);

        // Need to obtain the file name to determine term
        String[] split = path.split("/");
        String fileName = split[split.length - 1];
        int term = getTerm(fileName.substring(0, fileName.length() - 4));

        scanner.nextLine(); // Skip the first row which contain headers
        while (scanner.hasNextLine()) {
            CourseGPA grade = makeClass(scanner.nextLine(), term);
            if (grade != null) courses.add(grade);
        }

        scanner.close();
        return courses;
    }

    /**
     * Outputs class gpa data and search options into an output file
     *
     * @param path path of the grade distribution csv
     * @throws Exception
     */
    public void outputTermDataFile(String path) throws Exception {
        List<CourseGPA> courseGrades = this.parseOfficialTermFile(path);
        String[] split = path.split("/");
        String fileName = split[split.length - 1];
        int term = getTerm(fileName.substring(0, fileName.length() - 4));
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("src/main/resources/data/grades/" + term + ".csv"), "utf-8"))) {
            for (CourseGPA courseGPA : courseGrades) {
                com.pscheduler.server.model.CourseGPA gpa = (com.pscheduler.server.model.CourseGPA) courseGPA;
                writer.write(gpa.getSubject() + "," + gpa.getCourseNumber() + "," + gpa.getName() + "," +
                             gpa.getInstructor() + "," + gpa.getCrn() + "," + gpa.getCredits() + "," +
                             gpa.getGpa() + "," + gpa.getStudents() + "," + gpa.getA() + "," + gpa.getB() +
                             "," + gpa.getC() + "," + gpa.getD() + "," + gpa.getF() + "," + gpa.getWithdraws()
                             + "\r\n");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        GradeParser parser = new GradeParser(com.pscheduler.server.model.CourseGPA.class);
        parser.outputTermDataFile(args[0]);
    }

}
