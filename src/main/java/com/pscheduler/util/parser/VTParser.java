package com.pscheduler.util.parser;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

import com.pscheduler.util.CourseBuilderFactory;
import com.pscheduler.util.Course;

/**
 * The VTParser is the I/O handler for reading data from the timetable or parsing data from our database
 *
 * @author Phillip Ngo
 */
public class VTParser {
    
    private CourseBuilderFactory courseBuilderFactory;
    private HtmlFormGet vtForm;
    private int term;
    private String[] terms;
    private String[] subjects;

    // regex for extracting the next course from the database/timetable
    private final Pattern COURSE_PATTERN = Pattern.compile("\\d{5}[\\S\\s]*?(?=[ \\t\\r]*\\n[ \\t\\r]+\\d{5}|\\r\\nThis )");

    // regex for splitting a given COURSE_PATTERN's data
    private final String SPLIT_PATTERN = "(?<=\\S)\\s*[\\t\\n]\\s*(?=\\S)";

    /**
     * Default constructor instantiates the web form without filling any values
     *
     * @throws Exception
     */
    public VTParser(Class<?> courseType) throws Exception {
        init(courseType);
        subjects = new String[0];
    }

    /**
     * Constructor that instantiates the web form and fills out the term year
     *
     * @param term the html value to be selected
     * @throws Exception
     */
    public VTParser(Class<?> courseType, int term) throws Exception {
        init(courseType);
        this.setTerm(term);
    }

    /**
     * Initializes the web form and gets the term values
     *
     * @throws Exception
     */
    private void init(Class<?> courseType) throws Exception {
        this.vtForm = new HtmlFormGet("https://banweb.banner.vt.edu/ssb/prod/HZSKVTSC.P_ProcRequest", "ttform");
        this.terms = this.vtForm.getSelectOptionValues("TERMYEAR");
        this.term = 0;
        this.courseBuilderFactory = new CourseBuilderFactory(courseType);
    }

    /**
     * Selects the given term on the form
     *
     * @param term the html value of the form
     */
    public void setTerm(int term) {
        this.term = term;
        if (vtForm != null) {
            String termValue = term + "";
            if (termValue.equals(terms[0])) {
                int i = 1;
                while (!terms[i].equals(termValue)) {
                    i++;
                }
                vtForm.fillSelectField("TERMYEAR", i);
            } else if (Arrays.asList(terms).contains(termValue)) {
                vtForm.fillSelectField("TERMYEAR", termValue);
            }
            subjects = vtForm.getSelectOptionValues("subj_code");
        }
    }

    /**
     * A list of all subjects for the selected term
     *
     * @return a string array with all the subject names alphabetized
     */
    public String[] getSubjects() {
        return vtForm.getSelectOptions("subj_code");
    }

    /**
     * A list of all terms
     *
     * @return a string array with all the term names
     */
    public String[] getTerms() {
        return vtForm.getSelectOptions("TERMYEAR");
    }

    /**
     * A list of the subject values for the selected term
     *
     * @return a string array of all subject values
     */
    public String[] getSubjectValues() {
        return subjects;
    }

    /**
     * A list of all term values
     *
     * @return a string array with all term values
     */
    public String[] getTermValues() {
        String[] newTerms = new String[terms.length - 1];
        for (int i = 0; i < newTerms.length; i++) {
            newTerms[i] = terms[i + 1];
        }
        return newTerms;
    }

    /**
     * Parses a subject for the current selected term
     * Sorted in a hashmap by course number
     * For example: CS 2114
     * Key = 2114
     * Linked list contains all 2114 courses
     *
     * @param subj the subject to parse for
     * @return map of classes
     * @throws Exception
     */
    public List<Course> parseSubject(String subj) throws Exception {
        this.resetFields();
        vtForm.fillSelectField("subj_code", subjects[Arrays.binarySearch(subjects, subj)]);
        List<Course> list = parseCourseListing();
        return list;
    }

    /**
     * Parses for a specific subject and course number
     *
     * @param subj the subject
     * @param num  the course number
     * @return linked list of all classes with the subject and course number
     * @throws Exception
     */
    public List<Course> parseCourse(String subj, String num) throws Exception {
        this.resetFields();
        vtForm.fillSelectField("subj          _code", subj);
        vtForm.fillTextField("CRSE_NUMBER", num);
        return this.parseCourseListing();
    }

    /**
     * Parses for a specific crn
     *
     * @param crn the course request number
     * @return the VTCourse corresponding to the crn
     * @throws Exception
     */
    public Course parseCRN(String crn) throws Exception {
        this.resetFields();
        vtForm.fillTextField("crn", crn);
        List<Course> courses = this.parseCourseListing();
        if (courses.size() == 0) {
            return null;
        }
        return courses.get(0);
    }

    /**
     * The list of classes for a semester in a HashMap in the form
     * HashMap<K, HashMap<S, LinkedList<VTCourse>>> 0
     * Where the key, K, is the course subject (for example: ECE, AAEC, CS)
     * and holds another HashMap who's key, S, is course numbers (for example: 2014, 4763, 1114)
     * which holds a list of courses that have subject K and course number S (for example: ECE 2014, AAEC 4763, CS 1114)
     *
     * @return the HashMap of Classes
     * @throws Exception
     */
    public List<Course> parseTerm() throws Exception {
        this.resetFields();
        List<Course> list = new LinkedList<>();
        for (int i = 1; i < subjects.length; i++) {
            System.out.println(i + " " + subjects[i]); //debugging
            vtForm.fillSelectField("subj_code", subjects[i]);
            Exception exc = new Exception();
            int failCount = 0;
            while (exc != null) {
                try {
                    list.addAll(parseCourseListing());
                    exc = null;
                    failCount = 0;
                } catch (com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException e) {
                    System.out.println("connection failed, trying again");
                    exc = e;
                    failCount++;
                    if (failCount == 10) {
                        System.out.println("Connection Failed 10 Times, exiting");
                        System.exit(0);
                    }
                }
            }
        }
        return list;
    }

    /**
     * Resets all the form fields except term year
     */
    private void resetFields() {
        vtForm.fillSelectField("subj_code", "%");
        vtForm.fillTextField("crn", "");
        vtForm.fillTextField("CRSE_NUMBER", "");
    }

    /**
     * Parses the text from the printer friendly string taken from a course listing
     *
     * @return returns a list of courses
     * @throws Exception
     */
    private List<Course> parseCourseListing() throws Exception {
        List<Course> courses = new LinkedList<>();
        Matcher matcher = COURSE_PATTERN.matcher(getPrinterFriendly());
        while (matcher.find()) {
            Course course = makeClass(matcher.group());
            courses.add(course);
        }
        return courses;
    }

    /**
     * Creates a VTCourse from a string listing that has been extracted using the PATTERN variable
     *
     * @param listing the string to be parsed
     * @return the parsed course
     */
    private Course makeClass(String listing) {
        this.courseBuilderFactory.reset();
        String[] values = listing.split(SPLIT_PATTERN); //split listings based on column
        String[] subNum = values[1].split("-");
//        System.out.println("Parsing: " + Arrays.toString(values)); //Debugging

        this.courseBuilderFactory
            .term(this.term)
            .crn(Integer.parseInt(values[0]))
            .subject(subNum[0])
            .courseNumber(subNum[1])
            .name(values[2])
            .type(values[3].substring(0, 1))
            .credits(Integer.parseInt(values[4].substring(0, 1)))
            .capacity(Integer.parseInt(values[5]))
            .instructor(values[6]);

        int ind = 6;
        if (values.length > 7) {
            while (ind < values.length) {
                String days;
                String startTime;
                String endTime;
                String location;

                ind++;
                days = values[ind];
                ind++;
                if (values[ind].contains("ARR")) {
                    startTime = "ARR";
                    endTime = "ARR";
                } else {
                    startTime = values[ind];
                    endTime = values[ind + 1];
                    ind++;
                }
                ind++;
                location = values[ind];
                if (days != null) {
                    List<String> daysList = Arrays.asList(days.split(" "));
                    boolean valid = true;
                    for (String day : daysList) {
                        if (day.length() != 1) {
                            valid = false;
                        }
                    }
                    this.courseBuilderFactory.meeting(location, startTime, endTime, valid ? daysList : new ArrayList<>());
                }

                ind++;
                if (ind <= 11) {
                    this.courseBuilderFactory.exam(values[ind]);
                    ind++;
                }
            }
        }

        return courseBuilderFactory.build();
    }

    /**
     * submits form and obtains the printer friendly page
     *
     * @return a string containing the html page as text
     * @throws Exception
     */
    private String getPrinterFriendly() throws Exception {
        HtmlForm form;
        try {
            form = vtForm.pressButton("FIND class sections").getForms().get(1);
        } catch (Exception e) {
            return "";
        }
        HtmlSubmitInput button = form.getInputByValue("Printer Friendly List");
        HtmlPage printerFriendly = button.click();
        return printerFriendly.asText();
    }

    /**
     * Parses a term file that was created by the ouputFile() method
     *
     * @param path path to term file
     * @return a HashMap holding the subjects and classes for the term
     * @throws Exception
     */
    public List<Course> parseTermFile(String path) throws Exception {
        List<Course> courses = new ArrayList<>();
        File file = new File(path);
        Scanner scan = new Scanner(file);
        while (scan.hasNextLine()) {
            courses.add(makeClass(scan.nextLine()));
        }
        scan.close();
        return courses;
    }
}
