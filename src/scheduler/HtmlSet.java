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
        searchOptions();
        //numOptions();
        //subjOptions();
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
     * Creates the number options
     * @throws Exception
     */
    private static void searchOptions() throws Exception {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("WebContent/SelectOptions/SearchOptions.txt"), "utf-8"))) {
            
            for (String term : terms) {
                /*
                if (term.equals(terms.get(0))) {
                    writer.write("<select id='" + term + "search' data-size='5' class='selectpicker form-control' data-live-search='true'>\r\n");
                }
                else {
                    writer.write("<select id='" + term + "search' data-size='5' class='selectpicker form-control hide' data-live-search='true'>\r\n");
                }
                writer.write("<option style='font-style:italic' data-icon='glyphicon-search'>Search a Course or CRN (AHRM, 2014, CS 3114, 85149, etc.)</option>\r\n");
                */
                
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
                    //String[] courses = filterClasses(classes);
                    if (classes == null) {
                        continue;
                    }
                    for (String s : classes.keySet()) {
                        for (VTCourse c : classes.get(s)) {
                            /*
                            writer.write("<option data-tokens='" + c.getCRN() + "' "
                                       + "data-content=\"<button class='btn btn-default btn-sm' type='button'>Add</button> "
                                       + c.getCRN() + " / " + c.getSubject() + " " + c.getNum() + " / " + c.getClassType() + " / " + "<i>" + c.getName() + "</i> / " + c.getProf() + "\""
                                       + " disabled/>\r\n");
                                       */
                            writer.write("<li data-name='" + c.getCRN() + "' style='display:none;'> <button class='btn btn-default btn-sm' type='button'>Add</button> "
                                    + c.getCRN() + " / " + c.getSubject() + " " + c.getNum() + " / " + c.getClassType() + " / " + "<i>" + c.getName() + "</i> / " + c.getProf() + "\""
                                    + " </li>\r\n");
                        }
                    }
                }
                //writer.write("</select>");
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
