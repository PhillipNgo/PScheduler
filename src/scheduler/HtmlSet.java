package scheduler;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;

public class HtmlSet {
    
    public static void termOptions() {
        try {
            java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
            System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
            HtmlGet html = new HtmlGet("https://banweb.banner.vt.edu/ssb/prod/HZSKVTSC.P_ProcRequest", "ttform");
            String[] optionNames = html.getSelectOptions("TERMYEAR");
            String[] optionValues = html.getSelectOptionValues("TERMYEAR");
            if (optionNames.length != optionValues.length) {
                throw new Exception("why not same length??");
            }
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("WebContent/SelectOptions/TermOptions.txt"), "utf-8"))) {
                for (int i = optionNames.length - 1; i > 1; i--) {
                    writer.write("<option value=\"");
                    writer.write(optionValues[i]);
                    writer.write("\">");
                    writer.write(optionNames[i]);
                    writer.write("</option>");
                    writer.write("\r\n");
                }
            }
        }
        catch (Exception e) {
            return;
        }   
    }
    
    public static void numOptions() {
        try {
            java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
            System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
            HtmlGet html = new HtmlGet("https://banweb.banner.vt.edu/ssb/prod/HZSKVTSC.P_ProcRequest", "ttform");
            String[] subjects;
            String[] terms = html.getSelectOptionValues("TERMYEAR");
            
            ScheduleMaker parser = new ScheduleMaker();
            String listings;
            LinkedList<Class> classes;
            String[] nums, split;
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("WebContent/SelectOptions/NumberOptions.txt"), "utf-8"))) {
                for (int i = 1; i < terms.length; i++) {
                    html = new HtmlGet("https://banweb.banner.vt.edu/ssb/prod/HZSKVTSC.P_ProcRequest", "ttform");
                    html.fillSelectField("TERMYEAR", terms[i]);
                    subjects = html.getSelectOptionValues("subj_code");
                    for (int j = 1; j < subjects.length; j++) {   
                        html.fillSelectField("subj_code", subjects[j]);
                        listings = html.getPrinterFriendly();
                        classes = parser.parseClasses(listings);
                        nums = filterClasses(classes);
                        if (i == 3 && j == 1) {
                            writer.write("<select name=\"" + terms[i] + subjects[j] + "\" id=\"number\">\r\n");
                        }
                        else {
                            writer.write("<select name=\"" + terms[i] + subjects[j] + "\" style=\"display: none;\">\r\n");
                        }
                        writer.write("<option value=\"none\" disabled selected value>----</option>\r\n");
                        for (int k = 0; k < nums.length; k++) {
                            split = nums[k].split("--");
                            writer.write("<option name=\"");
                            writer.write(split[1]);
                            writer.write("--");
                            writer.write(split[2]);
                            writer.write("--");
                            writer.write(split[3]);
                            writer.write("\" value=\"");
                            writer.write(terms[i]);
                            writer.write(subjects[j]);
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
        catch (Exception e) {
            return;
        }   
    }
    
    private static String[] filterClasses(LinkedList<Class> list) {
        LinkedList<String> classList = new LinkedList<String>();
        Class currClass;
        String s;
        for (int i = 0; i < list.size(); i++) {
            currClass = list.get(i);
            s = currClass.getNum() + "--" + currClass.getName();
            boolean added = false;;
            for (int j = 0; j < classList.size(); j++) {
                String curr = classList.get(j);
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
        
        String[] copy = new String[classList.size()];
        for (int i = 0; i < classList.size(); i++) {
            copy[i] = classList.get(i);
        }

        return copy;
    }
    
    public static void main(String[] args) {
        termOptions();
        numOptions();
    }
}
