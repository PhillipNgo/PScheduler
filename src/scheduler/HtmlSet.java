package scheduler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Scanner;

import javax.servlet.jsp.JspWriter;

/**
 * Writes to a JSP file
 * @author Phillip Ngo
 *
 */
public class HtmlSet {
    
    private static LinkedList<String> terms;
    private JspWriter writer;
    
    /**
     * Constructor
     * @param writer the page to write to
     */
    public HtmlSet(JspWriter writer) {
        this.writer = writer;
        terms = new LinkedList<>();
    }
    
    /**
     * Creates the terms options
     * @throws Exception
     */
    public void termOptions() throws Exception {
        Scanner scan;
        try {
            scan = new Scanner(new File("webapps/ROOT/Database/terms.txt"));
        }
        catch (Exception e) {
            scan = new Scanner(new File("WebContent/Database/terms.txt"));
        }
        while (scan.hasNextLine()) {
            String[] values = scan.nextLine().split("/");
            writer.print("<option value=\"");
            writer.print(values[1]);
            writer.print("\">");
            writer.print(values[0]);
            writer.print("</option>");
            writer.print("\r\n");
            terms.add(values[1]);
        }
        scan.close();
    }
    
    public void searchOptions() throws Exception {
        Scanner scan;
        try {
            scan = new Scanner(new File("webapps/ROOT/Database/options.txt"));
        }
        catch (Exception e) {
            scan = new Scanner(new File("WebContent/Database/options.txt"));
        }
        while (scan.hasNextLine()) {
            writer.print(scan.nextLine());
        }
        scan.close();
    }
    
        
    
    /**
     * Creates the search options
     * @throws Exception
     */
    public static void createSearchOptions() throws Exception {
        try (Writer output = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("WebContent/Database/options.txt"), "utf-8"))) {
            for (String term : terms) {
                HashMap<String, HashMap<String, LinkedList<VTCourse>>> list;
                list = VTParser.parseTermFile(term);
                for (String subject : list.keySet()) {
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
                            output.write("<li id='crn' class='list-group-item' style='display:none;'>"
                                    + "<button name='" + c.getCRN() + "' class='btn btn-default btn-sm crnb' type='button'>Add</button> " 
                                    + c.getCRN() + " / " + c.getSubject() + " " + c.getNum() + " - " + c.getName() + " / "
                                    + longClassType(c.getClassType()) + " / " + c.getProf());
                            Time t = c.getTimeSlot();
                            if (t != null) {
                                String[] days = c.getDays();
                                output.write(" / ");
                                for (int i = 0; i < days.length; i++) {
                                    output.write(days[i]);
                                }
                                output.write(" " + t.getStart() + " - " + t.getEnd());
                                t = c.getAdditionalTime();
                                if (t != null) {
                                    days = c.getAdditionalDays();
                                    output.write(" / ");
                                    for (int i = 0; i < days.length; i++) {
                                        output.write(days[i]);
                                    }
                                    output.write(" " + t.getStart() + " - " + t.getEnd());
                                }
                            }
                            output.write("</li>\r\n");
                        }
                        output.write("<li id='course' class='list-group-item' style='display:none;'>"
                                + "<button name='" + subject + s + "' class='btn btn-default btn-sm courseb' type='button'>Add</button> " 
                                + subject + " " + s + " - " + name + " / " + longClassType(types.get(0)));
                        for (int i = 1; i < types.size(); i++) {
                            output.write(", " + longClassType(types.get(i)));
                        }
                        output.write(" / " + profs.get(0));
                        for (int i = 1; i < profs.size(); i++) {
                            output.write(", " + profs.get(i));
                        }
                        output.write(" / " + crns.get(0));
                        for (int i = 1; i < crns.size(); i++) {
                            output.write(", " + crns.get(i));
                        }
                        output.write("</li>\r\n");
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
}
