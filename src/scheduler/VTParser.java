package scheduler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.Scanner;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

/**
 * VTParser parses the VT database by putting the data into data structures or outputting file data
 * 
 * @author Phillip Ngo
 */
public class VTParser {
    
    private HtmlFormGet vtForm;
    private String[] terms;
    private String[] subjects;
    
    /**
     * Default constructor instantiates the web form without filling any values
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
        }
        else {
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
        String[] newTerms = new String[terms.length-1];
        for (int i = 0; i < newTerms.length;i++) {
            newTerms[i] = terms[i+1];
        }
        return newTerms;
    }
    
    /**
     * Initializes the web form and gets the term values
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
     * @param num the course number
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
        }
        catch (Exception e) {}
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
            //System.out.println(i + " " + subjects[i]); //debugging
            vtForm.fillSelectField("subj_code", subjects[i]);
            list.put(subjects[i], parseCourseListing());
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
     * @param text the text to parse
     * @return returns a map of subjects and their courses
     * @throws TimeException
     * @throws Exception
     */
    private HashMap<String, LinkedList<VTCourse>> parseCourseListing() throws TimeException, Exception {
        HashMap<String, LinkedList<VTCourse>> map = new HashMap<>();
        String text = getPrinterFriendly();
        String[] values = text.split("\t");
        int length = values.length;
        int count = 1;
        String name, subject, num, prof, location, crn, start, end, type, addStart, addEnd, addLoc;
        name = subject = num = prof = location = crn = start = end = type = addStart = addEnd = addLoc = null;
        boolean hasTime = true;
        String[] days, addDays;
        days = addDays = null;
        int credits = 0;
        int i = 0;
        while (i < values.length && length != 1) {
            while (count < 12 && i < length) {
                while (i < length && ((values[i].trim().length() == 0) || (values[i].trim().charAt(0) == '*') ||
                       (count == 1 && values[i].trim().length() < 5))) {
                    while (values[i].trim().length() == 0) {
                        i++;
                    }
                    if (values[i].trim().charAt(0) == '*') {
                        if(Time.isTime(values[i + 2]) && Time.isTime(values[i + 3])) {
                            addDays = values[i + 1].split(" ");
                            addStart = values[i + 2];
                            addEnd = values[i + 3];
                            addLoc = values[i + 4];
                            i += 1;
                        }
                        else {
                            if (type.equals("L")) {
                                type = "H";
                            }
                            else {
                                throw new Exception("A class with type " + type + " has an additional time that is arr");
                            }
                        }
                        i += 4;
                    }
                    if (count == 1 && values[i].trim().length() < 5) {
                        i++;
                    }
                    
                }
                
                if ((!hasTime || location != null) && count == 1) {
                    this.addClass(map, crn, subject, num, name, type, credits, prof, days, start, end, location, addStart,
                                  addEnd, addLoc, addDays, hasTime);
                    name = subject = num = prof = location = crn = start = end = type = addStart = addEnd = addLoc = null;
                    days = addDays = null;
                    hasTime = true;
                }
                
                if (i >= length - 1) {
                    return map;
                }
           
                String currString = values[i].trim();
                switch (count) {
                    case 1: crn = currString.substring(currString.length()-5, currString.length());
                            break;
                    case 2: String[] split = currString.split("-");
                            subject = split[0];
                            num = split[1];
                            //System.out.println(subject + " " + crn); //debugging
                            break;                            
                    case 3: name = currString;
                            break;
                    case 4: type = currString.substring(0, 1);
                            try {
                                if (!Time.isTime(values[i+5])) {
                                    if (values[i+6].contains("EMPO")) {
                                        type = "E";
                                    }
                                    hasTime = false;
                                }
                            }
                            catch (ArrayIndexOutOfBoundsException e) {
                                hasTime = false;
                            }         
                            break;
                    case 5: credits = Integer.parseInt(currString.substring(0, 1));
                            break;
                    case 6: break;
                    case 7: prof = currString;
                            if (!hasTime) {
                                count = 12;
                                try {
                                    Integer.parseInt(values[i].substring(currString.length()-5, currString.length()));
                                    String[] profCRN = currString.split("\r\n");
                                    prof = profCRN[0];
                                    i--;
                                }
                                catch (Exception e) {
                                    i += 3;
                                }
                            }
                            break;
                    case 8: days = currString.split(" ");
                            break;
                    case 9: start = currString;
                            break;
                    case 10: end = currString;
                             break;
                    case 11: location = currString;
                             break;
                }
                count++;
                i++;
            }
            count = 1;
            
            /*// debugging 
            if (crn.equals("80300")) {
                System.out.print(""); 
            }
            */
        }
        if (crn != null) {
            this.addClass(map, crn, subject, num, name, type, credits, prof, days, start, end, location, addStart,
                          addEnd, addLoc, addDays, hasTime);
        }
        return map;
    }
    
    /**
     * Helper method to add a class to the map
     * @param map the map to be added to 
     * @param crn the course request number
     * @param subject the course subject
     * @param num the course number
     * @param name the name of the course
     * @param type the type of course
     * @param credits amount of credits the course is
     * @param prof the professor
     * @param days the days the class is on
     * @param start the start time of the course
     * @param end the end time of the course
     * @param location the location of the course
     * @param addStart additional start time 
     * @param addEnd additional end time
     * @param addLoc additional location
     * @param addDays additional days
     * @param hasTime boolean to tell whether or not there is a valid time
     * @throws TimeException
     * @throws Exception
     */
    private void addClass(HashMap<String, LinkedList<VTCourse>> map, String crn, String subject, String num,
                          String name, String type, int credits, String prof, String[] days, String start, String end,
                          String location, String addStart, String addEnd, String addLoc, String[] addDays, boolean hasTime) 
                          throws TimeException, Exception {
        if (!map.containsKey(num)) {
            map.put(num, new LinkedList<VTCourse>());
        }
        if (hasTime) {
            if (addDays != null) {
                map.get(num).add(new VTCourse(name, subject, num, prof, location, crn, days, start,
                        end, credits, type, addDays, addStart, addEnd, addLoc));
            }
            else {
                map.get(num).add(new VTCourse(name, subject, num, prof, location, crn, days, start,
                                 end, credits, type));
            }
        }
        else {
            map.get(num).add(new VTCourse(name, subject, num, prof, crn, credits, type));
        }
        
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
        }
        catch (Exception e) {
            return "";
        }
        HtmlSubmitInput button = form.getInputByValue("Printer Friendly List");
        HtmlPage printerFriendly = button.click();
        String printerText = printerFriendly.asText();
        
        if (printerText.substring(0, 80).contains("Spring") ||
                printerText.substring(0, 80).contains("Winter")) {
            return printerText.substring(154, printerText.length() - 110);
        }
        else if (printerText.substring(0, 80).contains("Summer")) {
            if (printerText.substring(0, 80).contains("II")) {
                return printerText.substring(158, printerText.length() - 110);
            }
            return printerText.substring(157, printerText.length() - 110);
        }
        return printerText.substring(153, printerText.length() - 110);
    }
    
    /**
     * Outputs class data and search options into an output files
     * @param termYear the term value
     * @throws Exception
     */
    public static void outputTermDataFiles(String termYear) throws Exception {
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
                            writer.write(course.getName() + "\t" + course.getSubject() + "\t" + course.getNum()+ "\t" + course.getProf() +
                                         "\t" + course.getCRN() + "\t" + course.getCredits() + "\t" + course.getClassType());
                            if (course.getTimeSlot() != null) {
                                writer.write("\t" + course.getLocation() + "\t" + arrString(course.getDays()) + "\t" + 
                                             course.getTimeSlot().getStart() + "\t" + course.getTimeSlot().getEnd());
                            }
                            if (course.getAdditionalDays() != null) {
                                writer.write("\t" + course.getAdditionalLocation() + "\t" + arrString(course.getAdditionalDays()) + "\t" + 
                                        course.getAdditionalTime().getStart() + "\t" + course.getAdditionalTime().getEnd());
                            }
                            writer.write("\r\n");
                        }
                    }
                }
            }
        }
        
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("WebContent/Database/options.txt"), "utf-8"))) {
            for (String subject : subjects) {
                if (subject.equals("%")) {
                    continue;
                }
                HashMap<String, LinkedList<VTCourse>> classes = map.get(subject);
                if (classes == null) {
                    continue;
                }
                for (String s : classes.keySet()) {
                    LinkedList<String> types = new LinkedList<>();
                    LinkedList<String> profs = new LinkedList<>();
                    LinkedList<String> crns = new LinkedList<>();
                    String name = null;
                    for (VTCourse c : classes.get(s)) {
                        crns.add(c.getCRN());
                        if (types.indexOf(c.getClassType()) < 0) {
                            types.add(c.getClassType());
                        }
                        if (profs.indexOf(c.getProf()) < 0) {
                            profs.add(c.getProf());
                        }
                        if (name == null) {
                            name = c.getName();
                        }
                        writer.write(c.getCRN() + " / " + c.getSubject() + " " + c.getNum() + " - " + c.getName() + " / " + longClassType(c.getClassType()) + " / " + c.getProf());
                        Time t = c.getTimeSlot();
                        if (t != null) {
                            String[] days = c.getDays();
                            writer.write(" / ");
                            for (int i = 0; i < days.length; i++) {
                                writer.write(days[i]);
                            }
                            writer.write(" " + t.getStart() + " - " + t.getEnd());
                            t = c.getAdditionalTime();
                            if (t != null) {
                                days = c.getAdditionalDays();
                                writer.write(" / ");
                                for (int i = 0; i < days.length; i++) {
                                    writer.write(days[i]);
                                }
                                writer.write(" " + t.getStart() + " - " + t.getEnd());
                            }
                        }
                        writer.write("\r\n");
                    }
                    writer.write(subject + " " + s + " - " + name + " / " + longClassType(types.get(0)));
                    for (int i = 1; i < types.size(); i++) {
                        writer.write(", " + longClassType(types.get(i)));
                    }
                    writer.write(" / " + profs.get(0));
                    for (int i = 1; i < profs.size(); i++) {
                        writer.write(", " + profs.get(i));
                    }
                    writer.write(" / " + crns.get(0));
                    for (int i = 1; i < crns.size(); i++) {
                        writer.write(", " + crns.get(i));
                    }
                    writer.write("\r\n");
                }
            }
        }
    }
    
    /**
     * Converts a class type letter into its long for
     * @param type the class type
     * @return the long form
     */
    private static String longClassType(String type) {
        switch (type) {
            case "L": return "Lecture";
            case "B": return "Lab";
            case "C": return "Recitation";
            case "H": return "Hybrid";
            case "E": return "Emporium";
            case "O": return "Online";
            case "I": return "Independent Study";
            case "R": return "Research";
            default:  return "bug";
        }
    }
    
    /**
     * Parses a term file that was created by the ouputFile() method
     * @param termYear the term file name
     * @return a HashMap holding the subjects and classes for the term
     * @throws Exception
     */
    public static HashMap<String, HashMap<String, LinkedList<VTCourse>>> parseTermFile(String termYear) throws Exception {
        HashMap<String, HashMap<String, LinkedList<VTCourse>>> map = new HashMap<>();
        File file;
        if (new File("WebContent").exists()) { 
            file = new File("WebContent/Database/" + termYear + ".txt");
        }
        else {
            file = new File("webapps/ROOT/Database/" + termYear + ".txt");
        }
        Scanner scan = new Scanner(file);
        while (scan.hasNextLine()) {
            String[] data = scan.nextLine().split("\t");
            VTCourse course;
            if (data.length == 7) {
                course = new VTCourse(data[0], data[1], data[2], data[3], data[4], Integer.parseInt(data[5]), data[6]);
            }
            else if (data.length == 11) {
                course = new VTCourse(data[0], data[1], data[2], data[3], data[7], data[4], data[8].split(" "),
                                      data[9], data[10], Integer.parseInt(data[5]), data[6]);
            }
            else {
                course = new VTCourse(data[0], data[1], data[2], data[3], data[7], data[4], data[8].split(" "), data[9], data[10], 
                                      Integer.parseInt(data[5]), data[6], data[12].split(" "), data[13], data[14], data[11]);
            }
            
            HashMap<String, LinkedList<VTCourse>> subjList;
            String subj = course.getSubject();
            if (!map.containsKey(subj)) {
                subjList = new HashMap<String, LinkedList<VTCourse>>();
                map.put(subj, subjList);
            }
            else {
                subjList = map.get(subj);
            }
            
            LinkedList<VTCourse> list;
            String num = course.getNum();
            if (!subjList.containsKey(num)) {
                list = new LinkedList<>();
                subjList.put(num, list);
            }
            else {
                list = subjList.get(num);
            }
            
            list.add(course);
        }
        scan.close();
        return map;
    }
    
    /**
     * Helper method for outputFile() that puts a day arr formatted with spaces between entries
     * @param arr the array to separate
     * @return the string containing the separated entries
     */
    private static String arrString(String[] arr) {
        String s = "";
        for (String str : arr) {
            s += str;
            if (!str.equals(arr[arr.length - 1])) {
                s += " ";
            }
        }
        return s;
    }
    
    public static void main(String[] args) throws Exception {
        VTParser.outputTermDataFiles("201701");
    }
}
