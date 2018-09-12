package schedule;

import java.awt.EventQueue;
import java.awt.event.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.logging.Level;
import javax.swing.*;

import java.awt.Font;

/**
 * This represents the GUI for the VT_Scheduler Program
 * 
 * @author Phillip Ngo
 * @version 1.0
 */
public class Scheduler_GUI {

    private JFrame frmScheduleMaker;
    private JComboBox<String>[] subjects;
    private JTextField[] courseNumbers;
    private JTextField[] crns;
    private JTextField startTime;
    private JTextField endTime;
    private JComboBox<String> freeDay;
    private int currSchedule;
    private JTextArea scheduleText;
    private ScheduleMaker generator;
    private LinkedList<Schedule> schedules;
    private JComboBox<String> term;
    private final String[] subject_list = new String[]{
            "None", "AAEC - Agricultural and Applied Economics", "ACIS - Accounting and Information Systems", 
            "AFST - Africana Studies", "AHRM - Apparel, Housing, and Resource Management", "AINS - American Indian Studies",
            "ALCE - Agricultural, Leadership, and Community Education", "ALHR - Adult Learning and Human Resource Development", 
            "ALS - Agriculture and Life Sciences", "AOE - Aerospace and Ocean Engineering", "APS - Appalachian Studies", 
            "APSC - Animal and Poultry Sciences", "ARBC - Arabic", "ARCH - Architecture", "ART - Art and Art History", 
            "AS - Military Aerospace Studies", "ASPT - Alliance for Social, Political, Ethical, and Cultural Thought", 
            "AT - Agricultural Technology", "BC - Building Construction", "BCHM - Biochemistry", "BIOL - Biological Sciences", 
            "BIT - Business Information Technology", "BMES - Biomedical Engineering and Sciences", "BMSP - Biomedical Sciences and Pathobiology", 
            "BMVS - Biomedical and Veterinary Sciences", "BSE - Biological Systems Engineering", 
            "BTDM - Biomedical Technology Development and Management", "BUS - Business", "CEE - Civil and Environmental Engineering",
            "CHE - Chemical Engineering", "CHEM - Chemistry", "CHN - Chinese", "CINE - Cinema", "CLA - Classical Studies", 
            "CMDA - Computational Modeling and Data Analytics", "CNST - Construction", "COMM - Communication", "COS - College of Science", 
            "CRIM - Criminology", "CS - Computer Science", "CSES - Crop and Soil Environmental Sciences", "DASC - Dairy Science", 
            "ECE - Electrical and Computer Engineering", "ECON - Economics", "EDCI - Education, Curriculum and Instruction", 
            "EDCO - Counselor Education", "EDCT - Career and Technical Education", "EDEL - Educational Leadership", "EDEP - Educational Psychology",
            "EDHE - Higher Education", "EDIT - Instructional Design and Technology", "EDP - Environmental Design and Planning", 
            "EDRE - Education, Research and Evaluation", "EDTE - Technology Education", "ENGE - Engineering Education", "ENGL - English", "ENGR - Engineering\t",
            "ENSC - Environmental Science", "ENT - Entomology", "ESM - Engineering Science and Mechanics", "FA - Fine Arts", 
            "FIN - Finance", "FIW - Fish and Wildlife Conservation", "FL - Foreign Languages", "FOR - Forest Resources and Environmental Conservation", 
            "FR - French", "FST - Food Science and Technology", "GBCB - Genetics, Bioinformatics, Computational Biology", "GEOG - Geography", 
            "GEOS - Geosciences", "GER - German", "GIA - Government and International Affairs", "GR - Greek", "GRAD - Graduate School", 
            "HD - Human Development", "HEB - Hebrew", "HIST - History", "HNFE - Human Nutrition, Foods and Exercise", "HORT - Horticulture", 
            "HTM - Hospitality and Tourism Management", "HUM - Humanities", "IDS - Industrial Design", "IS - International Studies", "ISC - Integrated Science", 
            "ISE - Industrial and Systems Engineering", "ITAL - Italian", "ITDS - Interior Design", "JPN - Japanese", "JUD - Judaic Studies",
            "LAHS - Liberal Arts and Human Sciences", "LAR - Landscape Architecture", "LAT - Latin", "LDRS - Leadership Studies", 
            "MACR - Macromolecular Science and Engineering", "MASC - Mathematical Sciences", "MATH - Mathematics", "ME - Mechanical Engineering",
            "MGT - Management", "MINE - Mining Engineering", "MKTG - Marketing", "MN - Military Navy", "MS - Military Science (AROTC)", 
            "MSE - Materials Science and Engineering", "MTRG - Meteorology", "MUS - Music", "NANO - Nanoscience", "NEUR - Neuroscience", 
            "NR - Natural Resources", "NSEG - Nuclear Science and Engineering", "PAPA - Public Administration/Public Affairs", "PHIL - Philosophy", 
            "PHS - Population Health Sciences", "PHYS - Physics", "PORT - Portuguese", "PPWS - Plant Pathology, Physiology and Weed Science", 
            "PSCI - Political Science", "PSVP - Peace Studies", "PSYC - Psychology", "REAL - Real Estate", "RLCL - Religion and Culture", 
            "RTM - Research in Translational Medicine", "RUS - Russian", "SBIO - Sustainable Biomaterials", "SOC - Sociology", "SPAN - Spanish", 
            "STAT - Statistics", "STL - Science, Technology, & Law", "STS - Science and Technology Studies", "SYSB - Systems Biology", 
            "TA - Theatre Arts", "TBMH - Translational Biology, Medicine and Health", "UAP - Urban Affairs and Planning", "UH - University Honors",
            "UNIV - University Course Series", "VM - Veterinary Medicine", "WGS - Women's and Gender Studies"};
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Scheduler_GUI window = new Scheduler_GUI();
                    window.frmScheduleMaker.setVisible(true);
                } 
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * Create the application.
     */
    public Scheduler_GUI() throws Exception {
        initialize();
    }
    
    
    /**
     * Initialize the contents of the frame.
     */
    @SuppressWarnings("unchecked")
    private void initialize() throws Exception {
        frmScheduleMaker = new JFrame();
        frmScheduleMaker.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
        frmScheduleMaker.setTitle("Schedule Maker");
        frmScheduleMaker.setBounds(100, 100, 841, 650);
        frmScheduleMaker.setLocationRelativeTo(null);
        frmScheduleMaker.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmScheduleMaker.setResizable(false);
        
        frmScheduleMaker.getContentPane().setLayout(null);
        
        JLabel authorLabel = new JLabel("Phillip Ngo");
        authorLabel.setFont(new Font("Georgia", Font.ITALIC, 11));
        authorLabel.setBounds(762, 10, 62, 14);
        frmScheduleMaker.getContentPane().add(authorLabel);
        
        JLabel titleLabel = new JLabel("VT Schedule Maker");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        titleLabel.setBounds(302, 10, 226, 29);
        frmScheduleMaker.getContentPane().add(titleLabel);
        
        JLabel classesLabel = new JLabel("Classes:");
        classesLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        classesLabel.setBounds(20, 50, 73, 29);
        frmScheduleMaker.getContentPane().add(classesLabel);
        
        JLabel subjLabel = new JLabel("Subject:");
        subjLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        subjLabel.setBounds(20, 85, 46, 14);
        frmScheduleMaker.getContentPane().add(subjLabel);
        
        JComboBox<String> subj1 = new JComboBox<String>();
        subj1.setBounds(20, 102, 325, 20);
        
        JComboBox<String> subj2 = new JComboBox<String>();
        subj2.setBounds(20, 133, 325, 20);
        
        JComboBox<String> subj3 = new JComboBox<String>();
        subj3.setBounds(20, 166, 325, 20);
        
        JComboBox<String> subj4 = new JComboBox<String>();
        subj4.setBounds(20, 195, 325, 20);
        
        JComboBox<String> subj5 = new JComboBox<String>();
        subj5.setBounds(20, 226, 325, 20);
        
        JComboBox<String> subj6 = new JComboBox<String>();
        subj6.setBounds(20, 257, 325, 20);
        
        JComboBox<String> subj7 = new JComboBox<String>();
        subj7.setBounds(20, 288, 325, 20);
        
        JComboBox<String> subj8 = new JComboBox<String>();
        subj8.setBounds(20, 319, 325, 20);
        
        JComboBox<String> subj9 = new JComboBox<String>();
        subj9.setBounds(20, 350, 325, 20);
        
        subjects = new JComboBox[]{subj1, subj2, subj3, subj4, subj5, subj6, subj7, subj8, subj9};
        for (int i = 0; i < subjects.length; i++) {
            subjects[i].setModel(new DefaultComboBoxModel<String>(subject_list));
            subjects[i].setFont(new Font("Tahoma", Font.PLAIN, 12));
            frmScheduleMaker.getContentPane().add(subjects[i]);
        }
        
        JLabel courseLabel = new JLabel("Course #:");
        courseLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        courseLabel.setBounds(350, 85, 62, 14);
        frmScheduleMaker.getContentPane().add(courseLabel);
        
        JTextField num1 = new JTextField();
        num1.setBounds(350, 102, 86, 20);
        
        JTextField num2 = new JTextField();
        num2.setBounds(350, 133, 86, 20);
        
        JTextField num3 = new JTextField();
        num3.setBounds(350, 166, 86, 20);
        
        JTextField num4 = new JTextField();
        num4.setBounds(350, 195, 86, 20);
        
        JTextField num5 = new JTextField();
        num5.setBounds(350, 226, 86, 20);
        
        JTextField num6 = new JTextField();
        num6.setBounds(350, 257, 86, 20);
        
        JTextField num7 = new JTextField();
        num7.setBounds(350, 288, 86, 20);
        
        JTextField num8 = new JTextField();
        num8.setBounds(350, 319, 86, 20);
        
        JTextField num9 = new JTextField();
        num9.setBounds(350, 350, 86, 20);
        
        courseNumbers = new JTextField[]{num1, num2, num3, num4, num5, num6, num7, num8, num9};
        for (int i = 0; i < courseNumbers.length; i++) {
            courseNumbers[i].setFont(new Font("Tahoma", Font.PLAIN, 13));
            courseNumbers[i].setColumns(10);
            frmScheduleMaker.getContentPane().add(courseNumbers[i]);
        }
        
        JLabel restrictionsLabel = new JLabel("Restrictions:");
        restrictionsLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        restrictionsLabel.setBounds(472, 50, 114, 29);
        frmScheduleMaker.getContentPane().add(restrictionsLabel);
        
        JLabel crnLabel = new JLabel("Specific CRNs (Optional):");
        crnLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        crnLabel.setBounds(472, 85, 150, 14);
        frmScheduleMaker.getContentPane().add(crnLabel);
        
        JTextField crn1 = new JTextField();
        crn1.setBounds(471, 102, 86, 20);
        
        JTextField crn2 = new JTextField();
        crn2.setBounds(560, 102, 86, 20);
        
        JTextField crn3 = new JTextField();
        crn3.setBounds(649, 102, 86, 20);
        
        JTextField crn4 = new JTextField();
        crn4.setBounds(738, 102, 86, 20);
        
        JTextField crn5 = new JTextField();
        crn5.setBounds(471, 125, 86, 20);
        
        JTextField crn6 = new JTextField();
        crn6.setBounds(560, 125, 86, 20);
        
        JTextField crn7 = new JTextField();
        crn7.setBounds(649, 125, 86, 20);
        
        JTextField crn8 = new JTextField();
        crn8.setBounds(738, 125, 86, 20);
        
        crns = new JTextField[]{crn1, crn2, crn3, crn4, crn5, crn6, crn7, crn8};
        for (int i = 0; i < crns.length; i++) {
            crns[i].setFont(new Font("Tahoma", Font.PLAIN, 13));
            crns[i].setColumns(10);
            frmScheduleMaker.getContentPane().add(crns[i]);
        }
        
        JLabel startLabel = new JLabel("Earliest Start (Default: 12:00AM): ");
        startLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        startLabel.setBounds(472, 187, 198, 20);
        frmScheduleMaker.getContentPane().add(startLabel);
        
        startTime = new JTextField();
        startTime.setFont(new Font("Tahoma", Font.PLAIN, 13));
        startTime.setColumns(10);
        startTime.setBounds(680, 187, 114, 20);
        frmScheduleMaker.getContentPane().add(startTime);
        
        JLabel endLabel = new JLabel("Latest End (Default: 11:59PM): ");
        endLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        endLabel.setBounds(472, 216, 189, 20);
        frmScheduleMaker.getContentPane().add(endLabel);
        
        endTime = new JTextField();
        endTime.setFont(new Font("Tahoma", Font.PLAIN, 13));
        endTime.setColumns(10);
        endTime.setBounds(680, 216, 114, 20);
        frmScheduleMaker.getContentPane().add(endTime);
        
        JLabel freeDayLabel = new JLabel("Free Day:");
        freeDayLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
        freeDayLabel.setBounds(472, 247, 57, 20);
        frmScheduleMaker.getContentPane().add(freeDayLabel);
        
        freeDay = new JComboBox<String>();
        freeDay.setModel(new DefaultComboBoxModel<String>(new String[] {"None", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"}));
        freeDay.setFont(new Font("Tahoma", Font.PLAIN, 12));
        freeDay.setBounds(539, 247, 151, 20);
        frmScheduleMaker.getContentPane().add(freeDay);
        
        JButton generateButton = new JButton("Generate Schedules");
        generateButton.setFont(new Font("Tahoma", Font.BOLD, 15));
        generateButton.setBounds(472, 327, 253, 45);
        generateButton.addActionListener(new generateListener());
        frmScheduleMaker.getContentPane().add(generateButton);
        
        JButton nextButton = new JButton(">");
        nextButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        nextButton.setBounds(779, 327, 45, 45);
        nextButton.addActionListener(new arrowListener(1));
        frmScheduleMaker.getContentPane().add(nextButton);
        
        JButton previousButton = new JButton("<");
        previousButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
        previousButton.setBounds(735, 327, 45, 45);
        previousButton.addActionListener(new arrowListener(-1));
        frmScheduleMaker.getContentPane().add(previousButton);
        
        scheduleText = new JTextArea();
        scheduleText.setFont(new Font("Monospaced", Font.PLAIN, 12));
        scheduleText.setEditable(false);
        JScrollPane scroll = new JScrollPane(scheduleText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(0, 380, 835, 241);
        frmScheduleMaker.getContentPane().add(scroll);
        
        JLabel termText = new JLabel("Select Term:");
        termText.setFont(new Font("Tahoma", Font.PLAIN, 13));
        termText.setBounds(472, 157, 75, 20);
        frmScheduleMaker.getContentPane().add(termText);
        
        String[] options = new String[0];
        try {
            HtmlGet form = new HtmlGet("https://banweb.banner.vt.edu/ssb/prod/HZSKVTSC.P_ProcRequest", "ttform");
            String[] totalOptions = form.getSelectOptions("TERMYEAR");
            options = new String[totalOptions.length - 1];
            for (int i = 1; i < totalOptions.length; i++) {
                options[i - 1] = totalOptions[i];
            }
        }
        catch (Exception e) {
            scheduleText.setText("unable to retrieve term years");
        }
        
        term = new JComboBox<String>();
        term.setModel(new DefaultComboBoxModel<String>(options));
        term.setFont(new Font("Tahoma", Font.PLAIN, 12));
        term.setBounds(553, 157, 241, 20);
        frmScheduleMaker.getContentPane().add(term);
        
        JButton excelButton = new JButton("Save as Excel");
        excelButton.addActionListener(new saveListener(1));
        excelButton.setBounds(472, 296, 115, 20);
        frmScheduleMaker.getContentPane().add(excelButton);
        
        JButton textButton = new JButton("Save as txt");
        textButton.setBounds(597, 296, 115, 20);
        textButton.addActionListener(new saveListener(2));
        frmScheduleMaker.getContentPane().add(textButton);
        
        currSchedule = 0;
        generator = new ScheduleMaker();
        schedules = new LinkedList<Schedule>();
    }
    
    /**
     * The listener class for the generate schedules button
     */
    private class generateListener implements ActionListener {
        /**
         * The action performed when the button is pressed
         */
        @Override
        public void actionPerformed(ActionEvent arg0) {
            try {
                generator = new ScheduleMaker();
                scheduleText.setText("Creating Schedules...");
                generator.setTerm((String) term.getSelectedItem());
                for (int i = 0; i < crns.length; i++) {
                    if (crns[i].getText().length() != 0) {
                        generator.addCRN(crns[i].getText());
                    }
                }
                
                for (int i = 0; i < courseNumbers.length; i++) {
                    if (courseNumbers[i].getText().length() != 0 && !((String)subjects[i].getSelectedItem()).equals("None")) {
                        String[] splitSubj = ((String)subjects[i].getSelectedItem()).split("-");
                        generator.addClass(splitSubj[0].trim(), courseNumbers[i].getText());
                    }
                }
                
                if (!((String) freeDay.getSelectedItem()).equals("None")) {
                    if (freeDay.getSelectedIndex() != 4) {
                        generator.setFreeDay(((String) freeDay.getSelectedItem()).substring(0, 1));
                    }
                    else {
                        generator.setFreeDay("R");
                    }
                }
                else {
                    generator.setFreeDay(null);
                }
                
                if (startTime.getText().length() != 0) {
                    generator.setStart(startTime.getText());
                }
                else {
                    generator.setStart("12:00AM");
                }
                
                if (endTime.getText().length() != 0) {
                    generator.setEnd(endTime.getText());
                }
                else {
                    generator.setEnd("11:59PM");
                }
            } 
            catch (Exception e) {
                scheduleText.setText(e.getMessage());
                return;
            }
            new Thread() {
                public void run() {
                    try {
                        schedules = generator.generateSchedules();
                        String text;
                        if (schedules.size() == 0) {
                            text = "There are no schedules matching your parameters.\r\n\r\n"
                                   + "One or more of the following may have occurred:\r\n\r\n"
                                   + "    1. The latest end time begins before the earliest start\r\n"
                                   + "    2. A class does not exist on the timetable\r\n"
                                   + "    3. A class does not meet the parameters\r\n"
                                   + "    4. Two or more classes may always conflict for the given parameters\r\n"
                                   + "    5. There are only Online/Emporium/Research/Independent study options available for a class.\r\n"
                                   + "       All of these are ignored\r\n";
                        }
                        else {
                            text = currentSchedule();
                        }
                        currSchedule = 0;
                        scheduleText.setText(text + onlineClasses());
                    }
                    catch (Exception e) {
                        scheduleText.setText(e.getMessage());
                    }
                }
            }.start();

        }
    } 
    
    /**
     * The current showing schedule
     * 
     * @return String of the current class
     */
    private String currentSchedule() {
        try {
            String text = "Schedule " + (currSchedule + 1) + " of " + schedules.size() + ":\r\n\r\n" + 
                          "Credits (" + schedules.get(currSchedule).getTotalCredits() + ")\r\n" +
                          schedules.get(currSchedule).toString();
            return text;
        }
        catch (Exception ex) {
            return "error in displaying current schedule";
        }
    }
    
    /**
     * The online classes found
     * 
     * @return String of the online classes
     */
    private String onlineClasses() {
        String text = "";
        LinkedList<Class> onlineClasses = generator.getOnlineClasses();
        if (!onlineClasses.isEmpty()) {
            text += "\r\nOnline options were found for the following courses:\r\n";
            for (int i = 0; i < onlineClasses.size(); i++) {
                if (i != 0) {
                    text += ", ";
                }
                text += onlineClasses.get(i).getCollege() + " " + onlineClasses.get(i).getNum();
            }
        }
        return text;
    }
    
    /** 
     * The listener for the Arrow buttons
     */
    private class arrowListener implements ActionListener {
        
        private int value;
        /**
         * Constructor for the arrow
         * 
         * @param value the direction of the arrow
         *        -1 for left
         *        1 for right
         */
        public arrowListener(int value) {
            this.value = value;
        }
        
        /**
         * The action performed when the button is pressed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            currSchedule += value;
            if(currSchedule < 0) {
                currSchedule = 0;
            }
            else if(currSchedule >= schedules.size()) {
                currSchedule = schedules.size() - 1;
            }
            scheduleText.setText(currentSchedule() + onlineClasses());
        }
    }
    
    /** 
     * The listener for the save buttons
     */
    private class saveListener implements ActionListener {
        
        private String fileName;
        private int saveType;
        
        /**
         * Constructor 
         * 
         * @param saveType the type of file saved as
         *        1 for Excel
         *        2 for Text
         */
        public saveListener(int saveType) {
            this.saveType = saveType;
        }
        
        /**
         * The action performed when the button is pressed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (schedules.size() == 0) {
                scheduleText.setText("Please generate schedules before saving");
            }
            else {
                scheduleText.setText("Generating file...");
                new Thread() {
                    public void run() {
                        if (saveType == 1) {
                            fileName = System.getProperty("user.dir") + "\\" + "ExcelSchedules.xls";
                        }
                        else {
                            fileName = System.getProperty("user.dir") + "\\" + "TextSchedules.txt";
                        }
                        
                        try {
                            String suffix = "";
                            String[] fileSplit = fileName.split("\\.");
                            int fileNum = 0;
                            while (new File(fileSplit[0] + suffix + "." + fileSplit[1]).exists()) {
                                fileNum++;
                                suffix = " (" + fileNum + ")";
                                if (!new File(fileSplit[0] + suffix + "." + fileSplit[1]).exists()) {
                                    fileName = fileSplit[0] + suffix + "." + fileSplit[1];
                                }
                            }
                            
                            if (saveType == 1) {
                                ExcelSchedule excelGenerator = new ExcelSchedule(schedules.toArray());
                                excelGenerator.getExcelFile(fileName);
                            }
                            else {
                                PrintWriter textGenerator = new PrintWriter(fileName);
                                textGenerator.println(generator);
                                textGenerator.close();
                            }
                            
                            scheduleText.setText("The file was successfully generated at: " + fileName +
                                                 "\r\n\r\nPress one of the arrow buttons to return to the schedules");
                        }
                        catch (Exception ex) {
                            scheduleText.setText("failed to generate the file for an unknown reason" + 
                                                 "\r\n\r\nPress one of the arrow buttons to return to the schedules");
                        }
                    }
                }.start();
            }
        }
    }
}
