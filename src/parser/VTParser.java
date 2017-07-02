package parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

import ds.LinkedList;
import scheduler.VTCourse;
import time.Time;
import time.TimeException;

/**
 * The VTParser is the I/O handler for reading data from the timetable or parsing data from our database
 *
 * @author Phillip Ngo
 */
public class VTParser {

    private HtmlFormGet vtForm;
    private String[] terms;
    private String[] subjects;

    //regex for extracting the next course from the database/timetable
    private final Pattern PATTERN = Pattern.compile("\\d{5}[\\S\\s]*?(?=[ \\t\\r]*\\n[ \\t\\r]+\\d{5}|\\r\\nThis )");

    /**
     * Default constructor instantiates the web form without filling any values
     *
     * @throws Exception
     */
    public VTParser() throws Exception {
        init();
        subjects = new String[0];
    }

    /**
     * Constructor that instantiates the web form and fills out the term year
     *
     * @param termValue the html value to be selected
     * @throws Exception
     */
    public VTParser(String termValue) throws Exception {
        init();
        this.setTerm(termValue);
    }

    /**
     * Selects the given term on the form
     *
     * @param termValue the html value of the form
     */
    public void setTerm(String termValue) {
        if (termValue.equals(terms[0])) {
            int i = 1;
            while (!terms[i].equals(termValue)) {
                i++;
            }
            vtForm.fillSelectField("TERMYEAR", i);
        } else {
            vtForm.fillSelectField("TERMYEAR", termValue);
        }
        subjects = vtForm.getSelectOptionValues("subj_code");
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
     * Initializes the web form and gets the term values
     *
     * @throws Exception
     */
    private void init() throws Exception {
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        vtForm = new HtmlFormGet("https://banweb.banner.vt.edu/ssb/prod/HZSKVTSC.P_ProcRequest", "ttform");
        terms = vtForm.getSelectOptionValues("TERMYEAR");
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
    public HashMap<String, LinkedList<VTCourse>> parseSubject(String subj) throws Exception {
        this.resetFields();
        vtForm.fillSelectField("subj_code", subjects[Arrays.binarySearch(subjects, subj)]);
        HashMap<String, LinkedList<VTCourse>> list = parseCourseListing();
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
    public LinkedList<VTCourse> parseCourse(String subj, String num) throws Exception {
        this.resetFields();
        vtForm.fillSelectField("subj_code", subj);
        vtForm.fillTextField("CRSE_NUMBER", num);
        return this.parseCourseListing().get(num);
    }

    /**
     * Parses for a specific crn
     *
     * @param crn the course request number
     * @return the VTCourse corresponding to the crn
     * @throws TimeException
     * @throws Exception
     */
    public VTCourse parseCRN(String crn) throws TimeException, Exception {
        this.resetFields();
        vtForm.fillTextField("crn", crn);
        HashMap<String, LinkedList<VTCourse>> map = this.parseCourseListing();
        VTCourse c = null;
        try {
            c = map.get(map.keySet().iterator().next()).get(0);
        } catch (Exception e) {
        }
        return c;
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
    public HashMap<String, HashMap<String, LinkedList<VTCourse>>> parseTerm() throws Exception {
        this.resetFields();
        HashMap<String, HashMap<String, LinkedList<VTCourse>>> list = new HashMap<>();
        for (int i = 1; i < subjects.length; i++) {
            System.out.println(i + " " + subjects[i]); //debugging
            vtForm.fillSelectField("subj_code", subjects[i]);
            Exception exc = new Exception();
            int failCount = 0;
            while (exc != null) {
                try {
                    list.put(subjects[i], parseCourseListing());
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
     * @return returns a map of subjects and their courses
     * @throws TimeException
     * @throws Exception
     */
    private HashMap<String, LinkedList<VTCourse>> parseCourseListing() throws TimeException, Exception {
        HashMap<String, LinkedList<VTCourse>> map = new HashMap<>();
        Matcher matcher = PATTERN.matcher(getPrinterFriendly());
        while (matcher.find()) {
            VTCourse course = makeClass(matcher.group());
            if (!map.containsKey(course.getNum())) {
                map.put(course.getNum(), new LinkedList<VTCourse>());
            }
            map.get(course.getNum()).add(course);
        }

        return map;
    }

    /**
     * Creates a VTCourse from a string listing that has been extracted using the PATTERN variable
     *
     * @param listing the string to be parsed
     * @return
     * @throws Exception
     */
    public static VTCourse makeClass(String listing) throws Exception {
        String[] values = listing.split("(?<=\\S)\\s*[\\t\\n]\\s*(?=\\S)"); //split listings based on column
        //System.out.println("Parsing: " + Arrays.toString(values)); //Debugging
        String crn = values[0];
        String[] subNum = values[1].split("-");
        String subject = subNum[0];
        String number = subNum[1];
        String name = values[2];
        String type = values[3].substring(0, 1);
        int credits = Integer.parseInt(values[4].substring(0, 1));
        int capacity = Integer.parseInt(values[5]);
        String prof = values[6];
        LinkedList<String[]> days = new LinkedList<>();
        LinkedList<Time> times = new LinkedList<>();
        LinkedList<String> locs = new LinkedList<>();
        String exam = "00X"; //default value if there is no exam code
        int ind = 6;
        if (values.length > 7) {
            while (ind < values.length) {
                ind++;
                if (values[ind].contains("ARR")) {
                    days.add(null);
                } else {
                    days.add(values[ind].split(" "));
                }
                ind++;
                if (values[ind].contains("ARR")) {
                    times.add(null);
                } else {
                    try {
                        times.add(new Time(values[ind], values[ind + 1]));
                        ind++;
                    } catch (TimeException te) {
                        times.add(null);
                        System.out.println("Parsing time failed: making null");
                    }
                }
                ind++;
                locs.add(values[ind]);
                ind++;
                if (ind <= 11) {
                    exam = values[ind];
                    ind++;
                }
            }
        }

        if (days.size() == 0) {
            days.add(null);
            times.add(null);
            locs.add("(ARR)");
        }

        return new VTCourse(crn, subject, number, name, type, credits, capacity, prof, days, times, locs, exam);
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
     * Outputs class data and search options into an output files
     *
     * @param termYear the term value
     * @throws Exception
     */
    public static void outputTermDataFile(String termYear) throws Exception {
        VTParser parser = new VTParser(termYear);
        HashMap<String, HashMap<String, LinkedList<VTCourse>>> map = parser.parseTerm();
        String[] subjects = parser.getSubjectValues();

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("WebContent/Database/" + termYear + ".txt"), "utf-8"))) {
            for (String subject : subjects) {
                HashMap<String, LinkedList<VTCourse>> list = map.get(subject);
                if (list != null) {
                    for (String key : list.keySet()) {
                        for (VTCourse course : list.get(key)) {
                            writer.write(course.getCRN() + "\t" + course.getSubject() + "-" + course.getNum() + "\t" +
                                    course.getName() + "\t" + course.getClassType() + "\t" + course.getCredits() + "\t" +
                                    course.getCapacity() + "\t" + course.getProf());
                            for (int i = 0; i < course.getTimes().size(); i++) {
                                writer.write("\t");

                                String[] days = course.getDays().get(i);
                                if (days != null) {
                                    for (int d = 0; d < days.length; d++) {
                                        writer.write(days[d]);
                                        if (d < days.length - 1) {
                                            writer.write(" ");
                                        }
                                    }
                                } else {
                                    writer.write("(ARR)");
                                }
                                writer.write("\t");

                                Time time = course.getTimes().get(i);
                                if (time != null) {
                                    writer.write(time.getStart() + "\t" + time.getEnd());
                                } else {
                                    writer.write("(ARR)");
                                }
                                writer.write("\t");

                                String loc = course.getLocations().get(i);
                                if (loc == null) {
                                    writer.write("(ARR)");
                                } else {
                                    writer.write(loc);
                                }

                                if (i == 0) {
                                    writer.write("\t" + course.getExam());
                                }

                                if (i < course.getTimes().size() - 1) {
                                    writer.write("\tfill");
                                }
                            }
                            writer.write("\r\n");
                        }
                    }
                }
            }
        }
    }

    /**
     * Parses a term file that was created by the ouputFile() method
     *
     * @param termYear the term file name
     * @return a HashMap holding the subjects and classes for the term
     * @throws Exception
     */
    public static HashMap<String, HashMap<String, LinkedList<VTCourse>>> parseTermFile(String termYear) throws Exception {
        HashMap<String, HashMap<String, LinkedList<VTCourse>>> map = new HashMap<>();
        File file;
        if (new File("WebContent").exists()) {
            file = new File("WebContent/Database/" + termYear + ".txt");
        } else {
            file = new File("webapps/ROOT/Database/" + termYear + ".txt");
        }
        Scanner scan = new Scanner(file);
        while (scan.hasNextLine()) {
            // Printing for debugging 
            /*String line = scan.nextLine();
            System.out.println("Parsing: " + Arrays.toString(line.split("\t")));
            VTCourse course = makeClass(line); */
            VTCourse course = makeClass(scan.nextLine());

            HashMap<String, LinkedList<VTCourse>> subjList;
            String subj = course.getSubject();
            if (!map.containsKey(subj)) {
                subjList = new HashMap<String, LinkedList<VTCourse>>();
                map.put(subj, subjList);
            } else {
                subjList = map.get(subj);
            }

            LinkedList<VTCourse> list;
            String num = course.getNum();
            if (!subjList.containsKey(num)) {
                list = new LinkedList<>();
                subjList.put(num, list);
            } else {
                list = subjList.get(num);
            }

            list.add(course);
        }
        scan.close();
        return map;
    }

    /**
     * Manually update the database
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //VTParser.outputTermDataFile("201701");
        VTParser.outputTermDataFile("201709");
    }
}
