package com.pscheduler.util.parser;


import com.pscheduler.server.model.CourseGPA;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for reading data from the Grade Distribution Files and parsing them
 *
 * @author Francis Nguyen
 */
public class GradeParser {


    /**
     * Parses a term file containing GPA data
     *
     * @param path path to term file
     * @return list containing all the parsed gpa data
     */
    public List<CourseGPA> parseTermFile(String path) {
        List<CourseGPA> courses = new ArrayList<>();

        return courses;
    }

    /**
     * Parses every term file inside a folder
     *
     * @param path path to folder containing term year (which contains term files)
     * @return list holding the gpa data for each class
     */
    public List<CourseGPA> parseAllFiles(String path) {
        List<CourseGPA> courses = new ArrayList<>();
        File folder = new File(path);
        File[] files = folder.listFiles();

        for (int x = 0; x < files.length; x++) {
            if (files[x].isDirectory()) {
                System.out.println("Directory: " + files[x].getName());
                courses.addAll(parseAllFiles(path + "/" + files[x].getName()));
            } else if(files[x].isFile()) {
                System.out.println("File: " + files[x].getName());
                //courses.addAll(parseTermFile(path + "/" + files[x].getName()));
            }
        }

        return courses;
    }

}
