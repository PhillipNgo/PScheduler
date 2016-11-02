package scheduler;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
                new FileOutputStream("WebContent/SelectOptions/TermOptions.html"), "utf-8"))) {
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
        for (String term : terms) {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("WebContent/SelectOptions/" + term + "options.html"), "utf-8"))) {
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
                    if (classes == null) {
                        continue;
                    }
                    for (String s : classes.keySet()) {
                        LinkedList<String> types = new LinkedList<>();
                        LinkedList<String> profs = new LinkedList<>();
                        String name = null;
                        for (VTCourse c : classes.get(s)) {
                            if (types.indexOf(c.getClassType()) < 0) {
                                types.add(c.getClassType());
                            }
                            if (profs.indexOf(c.getProf()) < 0) {
                                profs.add(c.getProf());
                            }
                            if (name == null) {
                                name = c.getName();
                            }
                            writer.write("<li class='list-group-item' style='display:none'>"
                                    + "<button class='btn btn-default btn-sm' type='button'>Add</button> " 
                                    + c.getCRN() + " / " + c.getSubject() + " " + c.getNum() + " - " + c.getName() + " / "
                                    + longClassType(c.getClassType()) + " / " + c.getProf());
                            Time t = c.getTimeSlot();
                            if (t != null) {
                                writer.write(" / " + t.getStart() + " - " + t.getEnd());
                                t = c.getAdditionalTime();
                                if (t != null) {
                                    writer.write(" / " + t.getStart() + " - " + t.getEnd());
                                }
                            }
                            writer.write("</li>\r\n");
                        }
                        writer.write("<li class='list-group-item' style='display:none'><button class='btn btn-default btn-sm' type='button'>Add</button> " 
                                      + subject + " " + s + " - " + name + " / " + longClassType(types.get(0)));
                        for (int i = 1; i < types.size(); i++) {
                            writer.write(", " + longClassType(types.get(i)));
                        }
                        writer.write(" / " + profs.get(0));
                        for (int i = 1; i < profs.size(); i++) {
                            writer.write(", " + profs.get(i));
                        }
                        writer.write("</li>\r\n");
                    }
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
