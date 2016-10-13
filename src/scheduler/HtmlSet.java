package scheduler;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Outputs select options files
 * @author Phillip Ngo
 *
 */
public class HtmlSet {
    
    private static VTParser html;
    private static LinkedList<String> terms;
    
    /**
     * Outputs all files
     * @throws Exception
     */
    public static void createFiles() throws Exception {
        html = new VTParser();
        terms = new LinkedList<>();
        
        termOptions();
        numOptions();
        subjOptions();
    }
    
    /**
     * Creates the terms options
     * @throws Exception
     */
    private static void termOptions() throws Exception {       
        String[] optionNames = html.getTerms();
        String[] optionValues = html.getTermValues();
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("WebContent/SelectOptions/TermOptions.txt"), "utf-8"))) {
            for (int i = 1; i < optionNames.length-1; i++) {
                if (!optionNames[i].contains("Summer") && !optionNames[i].contains("Winter")) {
                    writer.write("<option value=\"");
                    writer.write(optionValues[i-1]);
                    writer.write("\">");
                    writer.write(optionNames[i]);
                    writer.write("</option>");
                    writer.write("\r\n");
                    terms.add(optionValues[i-1]);
                }
            }
        }
    }
    
    /**
     * Creates the subject options
     * @throws Exception
     */
    private static void subjOptions() throws Exception {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("WebContent/SelectOptions/SubjectOptions.txt"), "utf-8"))) {
            for (String term : terms) {
                html.setTerm(term);
                String[] subjectValues = html.getSubjectValues();
                String[] subjectNames = html.getSubjects();
                if (term.equals(terms.get(0))) {
                    writer.write("<select id=\"subjects\" name=\"" + term + "subjects\" class=\"optionfont\" onchange=\"displayNums(false)\">\r\n");
                }
                else {
                    writer.write("<select id=\"\" class=\"optionfont\" name=\"" + term + "subjects\""
                                + " style=\"display: none;\" onchange=\"displayNums(false)\">\r\n");
                }
                
                for (int i = 1; i < subjectNames.length; i++) {
                    writer.write("<option value=\"" + subjectValues[i] + "\">" + subjectNames[i] + "</option>\r\n");
                }
                writer.write("</select>");
            }
        }
    }
    
    /**
     * Creates the number options
     * @throws Exception
     */
    private static void numOptions() throws Exception {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("WebContent/SelectOptions/NumberOptions.txt"), "utf-8"))) {
            String[] optionNames = html.getTerms();
            int i = optionNames.length;
            for (String term : terms) {
                i--;
                if (optionNames[i].contains("Summer") || optionNames[i].contains("Winter")) {
                    continue;
                }
                html.setTerm(term);
                String[] subjects = html.getSubjectValues();
                HashMap<String, HashMap<String, LinkedList<VTCourse>>> list;
                try {
                    list = VTParser.parseTermFile(term);
                }
                catch(Exception e) {
                    list = html.parseTerm();
                }
                for (String subject : subjects) {
                    if (subject.equals("%")) {
                        continue;
                    }
                    HashMap<String, LinkedList<VTCourse>> classes = list.get(subject);
                    String[] nums = filterClasses(classes);
                    Arrays.sort(nums);
                    if (term.equals("201609") && subject.equals("AAEC")) {
                        writer.write("<select name=\"" + term + subject + "\" id=\"number\">\r\n");
                    }
                    else {
                        writer.write("<select name=\"" + term + subject + "\" style=\"display: none;\">\r\n");
                    }
                    writer.write("<option value=\"none\" disabled selected value>----</option>\r\n");
                    for (int k = 0; k < nums.length; k++) {
                        String[] split = nums[k].split("--");
                        writer.write("<option name=\"");
                        writer.write(split[1]);
                        writer.write("--");
                        writer.write(split[2]);
                        writer.write("--");
                        writer.write(split[3]);
                        writer.write("\" value=\"");
                        writer.write(term);
                        writer.write(subject);
                        writer.write(split[0]);
                        writer.write("\">");
                        writer.write(split[0]);
                        writer.write("</option>");
                        writer.write("\r\n");
                    }
                    writer.write("</select>\r\n");
                }
            }
        } 
    }
    
    /**
     * Filters classes into strings
     * @param classes the classes to be filtered
     * @return the string array of class information
     * @throws NumberFormatException
     * @throws Exception
     */
    private static String[] filterClasses(HashMap<String, LinkedList<VTCourse>> classes) throws NumberFormatException, Exception {
        LinkedList<String> classList = new LinkedList<String>();
        if (classes == null) {
            return new String[0];
        }
        for (String key : classes.keySet()) {
            LinkedList<VTCourse> list = classes.get(key);
            for (VTCourse currClass : list) {
                String s = currClass.getNum() + "--" + currClass.getName();
                boolean added = false;;
                for (String curr : classList) {
                    String[] currArr = curr.split("--");
                    if (curr.contains(s)) {
                        added = true;
                        if (!currArr[3].contains(currClass.getClassType())) {
                            classList.remove(s + "--" + currArr[2] + "--" + currArr[3]);
                            classList.add(s + "--" + Math.max(Integer.parseInt(currArr[2]), currClass.getCredits()) + "--" + currArr[3] + currClass.getClassType());
                        }
                        break;
                    }
                }
                if (!added) {
                    classList.add(s + "--" + currClass.getCredits() + "--" +  currClass.getClassType());
                }
            }
        }
        String[] copy = new String[classList.size()];
        for (int i = 0; i < classList.size(); i++) {
            copy[i] = classList.get(i);
        }

        return copy;
    }
    
    public static void main(String[] args) throws Exception {
        HtmlSet.createFiles();
    }
}
