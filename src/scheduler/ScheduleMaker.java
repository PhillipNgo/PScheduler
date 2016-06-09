package scheduler;

/**
 * ScheduleMaker represents all classes used to make a schedule from
 * its given classes
 * 
 * @author Phillip Ngo
 * @version 1.0
 */
public class ScheduleMaker {
    
    //Fields -----------------------------------------------------------------------------
    
    private LinkedList<Class> classes;
    private TimeSlot timeslot;
    private String freeDay;
    private LinkedList<Schedule> schedules;
    private LinkedList<Class> onlineClasses;
    private LinkedList<Class[]> classListings;
    private HtmlGet form;
    
    //Constructors -----------------------------------------------------------------------
    
    /**
     * Default Constructor instantiates the parameters with the default values
     */
    public ScheduleMaker() throws Exception {
        classes = new LinkedList<Class>();
        timeslot = new TimeSlot("8:00AM", "9:00PM");
        freeDay = null;
        schedules = new LinkedList<Schedule>();
        onlineClasses = new LinkedList<Class>();
        classListings = new LinkedList<Class[]>();
        form = new HtmlGet("https://banweb.banner.vt.edu/ssb/prod/HZSKVTSC.P_ProcRequest", "ttform");
    }
    
    /**
     * Constructor without schedule parameters
     * 
     * @param colleges colleges of the classes
     * @param classNums numbers that correspond with the colleges
     */
    public ScheduleMaker(String[] colleges, String[] classNums) throws Exception {
        this();
        if (colleges == null || classNums == null || colleges.length != classNums.length) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < classNums.length; i++) {
            try {
                Integer.parseInt(classNums[i]);
            }
            catch (NumberFormatException e) {
                throw new IllegalArgumentException();
            }
        }
        
        if (classNums.length != colleges.length) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < classNums.length; i++) {
            classes.add(new Class(colleges[i], classNums[i]));
        }
        classes = new LinkedList<Class>();
    }
    
    /**
     * Full constructor with Schedule parameters
     * 
     * @param colleges colleges of the classes
     * @param classNums corresponding numbers for the colleges
     * @param start minimum schedule start time
     * @param end max schedule end time
     * @param gap minimum gap between classes
     * @param freeDay day to be free
     */
    public ScheduleMaker(String[] colleges, String[] classNums, String term, 
                         String start, String end, int gap, String freeDay) throws Exception {
        this(colleges, classNums);
        if (TimeSlot.isTime(start) || TimeSlot.isTime(end) || gap < 0 || freeDay == null || !Timetable.isDay(freeDay)) {
            throw new IllegalArgumentException();
        }
        timeslot = new TimeSlot(start, end);
        try {
            setTermYear(term);
        }
        catch (Exception e) {
            
        }
        this.freeDay = freeDay;
    }
    
    //Setter Methods ---------------------------------------------------------------------
    
    /**
     * Sets the schedule start time
     * 
     * @param time time to start
     */
    public void setStart(String time) {
        if (time == null || !TimeSlot.isTime(time)) {
            throw new IllegalArgumentException(time + " is not a valid time");
        }
        timeslot.setStart(time);
    }
    
    /**
     * Sets the schedule end time
     * 
     * @param time time to end
     */
    public void setEnd(String time) {
        if (time == null || !TimeSlot.isTime(time)) {
            throw new IllegalArgumentException(time + " is not a valid time");
        }
        timeslot.setEnd(time);
    }
    
    /**
     * Sets the day to be free
     * 
     * @param freeDay day to be free
     */
    public void setFreeDay(String freeDay) {
        this.freeDay = freeDay;
    }
    
    /**
     * Sets the term
     * 
     * @param term the term to be set
     */
    public void setTerm(String term) throws Exception {
        setTermYear(term);
    }
    
    //Getter Methods ---------------------------------------------------------------------
    
    /**
     * The amount of schedules possible with the class set
     * 
     * @return the number of class permutations
     */
    public int getNumSchedules() {
        return schedules.size();
    }
    
    /**
     * The online classes encountered while generating
     * 
     * @return onlineClasses
     */
    public LinkedList<Class> getOnlineClasses() {
        return onlineClasses.createCopy();
    }
    
    //Methods ----------------------------------------------------------------------------
    
    /**
     * Adds a class to classes to be put in schedule
     * 
     * @param college college of the class
     * @param num the class number
     */
    public void addClass(String college, String num) {
        if (college == null) {
            throw new IllegalArgumentException();
        }
        classes.add(new Class(college, num));
    }
    
    /**
     * Removes a class from the ScheduleMaker
     * 
     * @param college college of the class
     * @param num the class number
     */
    public void removeClass(String college, String num) {
        if (college == null) {
            throw new IllegalArgumentException();
        }
        Class removed = new Class(college, num);
        classes.remove(removed);
    }
    
    /**
     * A specific class to put in the schedule
     * 
     * @param crn course request number of the class
     */
    public void addCRN(String crn) {    
        if (crn.length() != 5) {
            throw new IllegalArgumentException(crn + " is not a valid crn");
        }
        try {
            Integer.parseInt(crn);
        }
        catch (Exception e) {
            throw new IllegalArgumentException(crn + " is not a valid crn");
        }
        form.fillTextField("crn", crn);
        String listings = null;
        try {
            listings = form.getPrinterFriendly();
        }
        catch (Exception e) {
            
        }
        if (listings.length() == 0) {
            throw new IllegalArgumentException("CRN " + crn + " was not found on the timetable");
        }
        Class[] addCRN = new Class[]{this.parseClasses(listings).get(0)};
        classListings.add(addCRN);
        form.fillTextField("crn", "");
    }
    
    /**
     * Remove a specified class
     * 
     * @param crn course request number of the class
     */
    public void removeCRN(String crn) {
        if (crn.length() != 5) {
            throw new IllegalArgumentException(crn + " is not a valid crn");
        }
        try {
            Integer.parseInt(crn);
        }
        catch (Exception e) {
            throw new IllegalArgumentException(crn + " is not a valid crn");
        }
        for (int i = 0; i < classListings.size(); i++) {
            if (classListings.get(i).length == 1 && classListings.get(i)[0].getCRN().equals(crn)) {
                classListings.remove(classListings.get(i));
                return;
            }
        }
        throw new IllegalArgumentException("CRN " + crn + " is not on the list of added classes");
    }
    
    /**
     * Creates all permutations of Schedules based on 
     * parameters and puts them into an array
     * 
     * @return array of schedules
     */
    public LinkedList<Schedule> generateSchedules() throws Exception {
        if (classes.size() == 0 && classListings.size() == 0) {
            throw new IllegalStateException("No classes were inputted");
        }
        schedules.clear();
        getClassListings();
        createSchedules(new LinkedList<Class>(), 0, 0);
        LinkedList<Schedule> copy = schedules.createCopy();
        classListings.clear();
        classes.clear();
        return copy;
    }
    
    /**
     * Calls the toString of all schedules in the list
     * 
     * @return string of all schedules
     */
    @Override
    public String toString() {
        String s = "There are " + schedules.size() + " possible schedules matching your parameters:\r\n\r\n";
        s += schedules.toString();
        return s;
    }
    
    //Private Methods ----------------------------------------------------------------------
    
    /**
     * Checks if a list of classes can become a schedule
     * 
     * @param schedule the list of classes to be checked
     * @return true if there are no conflicts
     */
    private boolean isValidSchedule(LinkedList<Class> schedule) {
        for(int i = 0; i < schedule.size(); i++) {
            for (int j = i + 1; j < schedule.size(); j++) {
                if(schedule.get(i).conflicts(schedule.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Adds all valid Schedules to the schedules field
     * 
     * @param list the list used to create schedules
     * @param loopLevel the current class
     * @param index the current version of class
     */
    private void createSchedules(LinkedList<Class> list, int loopLevel, int index) {   
        if (loopLevel == classListings.size() - 1 && index < classListings.get(loopLevel).length) {
            LinkedList<Class> copy = list.createCopy();
            createSchedules(copy, loopLevel, index + 1);
            list.add(classListings.get(loopLevel)[index]);
            if (isValidSchedule(list)) {
                schedules.add(new Schedule(list));
            }
        }
        else if (loopLevel < classListings.size() - 1) {
            for (int i = 0; i < classListings.get(loopLevel).length; i++) {
                list.add(classListings.get(loopLevel)[i]);
                LinkedList<Class> copy = list.createCopy();
                createSchedules(copy, loopLevel + 1, index);
                list.remove(list.get(0));
            }
        }
    }
    
    /**
     * Compiles a list containing array of all class times for every class
     * 
     * @return LinkedList of class arrays, each holding all the class times for a certain class
     */
    private void getClassListings() throws Exception {
        for (int i = 0; i < classes.size(); i++) {
            classListings.add(pullClasses(classes.get(i)));
        }
    }
    
    /**
     * Sets the term year to its corresponding value
     * Ex: "Fall 2016"
     * 
     * @param termYear the term to be selected
     * @throws Exception IllegalArgumentException if the term does not exist
     */
    private void setTermYear(String termYear) throws Exception {
        form.fillSelectField("TERMYEAR", termYear);
    }
    
    /**
     * Pulls all the class times for the given class from the VT Timetable
     * 
     * @param c the given class
     * @return the array of all class times
     */
    private Class[] pullClasses(Class c) throws Exception {
        
        form.fillSelectField("subj_code", c.getCollege());
        form.fillTextField("CRSE_NUMBER", c.getNum());
        
        String listings;
        try {
            listings = form.getPrinterFriendly();
        }
        catch (Exception e) {
            throw new IllegalArgumentException(c.getCollege() + "-" + c.getNum() + " was not found on the timetable");
        }
        LinkedList<Class> list = parseClasses(listings);
        LinkedList<Class> classList = new LinkedList<Class>();
        separateClassTypes(list, c);
        
        Class currClass;
        for (int i = 0; i < list.size(); i++) {
            currClass = list.get(i);
            
            if (currClass.getCollege().equals(c.getCollege()) &&
                currClass.getTimeSlot().getStartNum() >= timeslot.getStartNum() &&
                currClass.getTimeSlot().getEndNum() <= timeslot.getEndNum() &&
                !containsDay(freeDay, currClass.getDays())){
               
                if (currClass.getAdditionalDays() == null && currClass.getAdditionalTime() == null 
                        && currClass.getAdditionalLocation() == null) {
                    classList.add(currClass);
                }
                else if (currClass.getAdditionalTime().getStartNum() >= timeslot.getStartNum() &&
                         currClass.getAdditionalTime().getEndNum() <= timeslot.getEndNum() &&
                         !containsDay(freeDay, currClass.getAdditionalDays())) {
                    classList.add(currClass);
                } 
            }
        }
        
        Class[] copy = new Class[classList.size()];
        for (int i = 0; i < classList.size(); i++) {
            copy[i] = classList.get(i);
        }

        return copy;
    }
    
    /**
     * If a class has two parts to sign up for i.e Recitation and Lecture
     * and they have the same course number i.e PHYS 2305 and PHYS 2305
     * This separates
     * 
     * @param originalList the aggregate list
     * @param c the class parameters
     */
    private void separateClassTypes(LinkedList<Class> originalList, Class c) {
        LinkedList<Class> list = new LinkedList<Class>();
        String type1 = null;
        int i = 0;
        Class currClass;
        while (i < originalList.size() && type1 == null) {
            currClass = originalList.get(i);
            if (currClass.getCollege().equals(c.getCollege()) &&
                    currClass.getTimeSlot().getStartNum() >= timeslot.getStartNum() &&
                    currClass.getTimeSlot().getEndNum() <= timeslot.getEndNum() &&
                    !containsDay(freeDay, currClass.getDays())){
                type1 = currClass.getClassType();
            }
            i++;
        } 
        if (type1 != null) {
            for (i = 0; i < originalList.size(); i++) {
                currClass = originalList.get(i);
                if (currClass.getCollege().equals(c.getCollege()) &&
                        currClass.getTimeSlot().getStartNum() >= timeslot.getStartNum() &&
                        currClass.getTimeSlot().getEndNum() <= timeslot.getEndNum() &&
                        !containsDay(freeDay, currClass.getDays()) && !currClass.getClassType().equals(type1)){
                    if (currClass.getAdditionalDays() == null && currClass.getAdditionalTime() == null 
                            && currClass.getAdditionalLocation() == null) {
                        list.add(originalList.remove(originalList.get(i)));
                        i--;
                    }
                    else if (currClass.getAdditionalTime().getStartNum() >= timeslot.getStartNum() &&
                             currClass.getAdditionalTime().getEndNum() <= timeslot.getEndNum() &&
                             !containsDay(freeDay, currClass.getAdditionalDays())) {
                        list.add(originalList.remove(originalList.get(i)));
                        i--;
                    } 
                }
            }
        }
        if (list.size() > 0) {
            Class[] copy = new Class[list.size()];
            for (i = 0; i < list.size(); i++) {
                copy[i] = list.get(i);
            }
            classListings.add(copy);
        }
    }
    
    /**
     * Finds if the freeDay is within the days a class is
     * 
     * @param find the day to be found
     * @param list the list of days
     * @return true if it is contained
     */
    private boolean containsDay(String day, String[] list) {
        if (day == null) {
            return false;
        }
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(day)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Creates a LinkedList of classes from the printer friendly text
     * 
     * @param text the text to be parsed
     * @return the list of classes parsed
     */
    public LinkedList<Class> parseClasses(String text) {
        LinkedList<Class> list = new LinkedList<Class>();
        String[] values = text.split("\t");
        int count = 1;
        String name, college, num, prof, location, crn, start, end, type;
        name = college = num = prof = location = crn = start = end = type = null;
        String[] days = null;
        int credits = 0;
        int i = 0;
        while (i < values.length-1) {
            while (count < 12 && i < values.length-1) {
                while ((values[i].trim().length() == 0) || (values[i].trim().charAt(0) == '*') ||
                       (count == 1 && values[i].trim().length() < 5)) {
                    while (values[i].trim().length() == 0) {
                        i++;
                    }
                    if (values[i].trim().charAt(0) == '*') {
                        if(TimeSlot.isTime(values[i + 2]) || TimeSlot.isTime(values[i + 3])) {
                            list.get(0).setAdditionalDays(values[i + 1].split(" "));
                            list.get(0).setAdditionalTime(new TimeSlot(values[i + 2], values[i + 3]));
                            list.get(0).setAdditionalLocation(values[i + 4]);
                            i += 1;
                        }
                        i += 4;
                    }
                    if (count == 1 && values[i].trim().length() < 5) {
                        i++;
                    }
                    if (i >= values.length-1) {
                        return list;
                    }
                }
                String currString = values[i].trim();
                switch (count) {
                    case 1: crn = currString.substring(currString.length()-5, currString.length());
                            break;
                    case 2: String[] split = currString.split("-");
                            college = split[0];
                            num = split[1];
                            break;                            
                    case 3: name = currString;
                            break;
                    case 4: type = currString.substring(0, 1);
                            if (type.equals("O") || type.equals("I") || type.equals("R")) {
                                count = 12;
                                try {
                                    String s = values[i+7].trim().substring(values[i+7].trim().length()-5, values[i+7].trim().length());
                                    if (Integer.parseInt(s) > 100) {
                                        i += 6;
                                    }
                                    else {
                                        i += 2;
                                    }
                                }
                                catch (Exception e) {
                                    try {
                                        if (values[i+7].trim().length() == 3 || values[i+7].trim().equals("?")) {
                                            i += 4;
                                        }
                                        else if (values[i+8].length() > 8 || values[i+8].trim().length() == 3) {
                                            i += 5;
                                        }
                                    }
                                    catch (Exception ex) {
                                        i += 1;
                                    }
                                    i += 2;
                                }
                            }
                            if ((type.charAt(0) == 'L' || type.charAt(0) == 'B') && !TimeSlot.isTime(values[i+5])) {
                                type = "I";
                                count = 12;
                                try {
                                    String s = values[i+7].trim().substring(values[i+7].trim().length()-5, values[i+7].trim().length());
                                    if (Integer.parseInt(s) > 100) {
                                        i += 6;
                                    }
                                    else {
                                        i += 2;
                                    }
                                }
                                catch (Exception e) {
                                    try {
                                        if (values[i+7].trim().length() == 3 || values[i+7].trim().equals("?")) {
                                            i += 4;
                                        }
                                        else if (values[i+8].length() > 8 || values[i+8].trim().length() == 3) {
                                            i += 5;
                                        }
                                    }
                                    catch (Exception ex) {
                                        i += 1;
                                    }
                                    i += 2;
                                }
                            }
                            break;
                    case 5: credits = Integer.parseInt(currString.substring(0, 1));
                            break;
                    case 6: break;
                    case 7: prof = currString;
                            break;
                    case 8: days = currString.split(" ");
                            break;
                    case 9: if (!TimeSlot.isTime(currString)) {
                                type = "I";
                                count = 12;
                            }
                            start = currString;
                            break;
                    case 10: end = currString;
                             break;
                    case 11: location = currString;
                             break;
                }
                count++;
                i++;
            }
            if (type.equals("O")) {
                Class online = new Class(college, num);
                boolean found = true;
                for (int j = 0; j < onlineClasses.size(); j++) {
                    if (onlineClasses.get(j).generalEquals(online)) {
                        found = false;
                    }
                }
                if (found == true || onlineClasses.isEmpty()) {
                    for (int j = 0; j < classes.size(); j++) {
                        if (classes.get(j).generalEquals(online)) {
                            onlineClasses.add(online);
                        }
                    }
                }
            }
            else if (!type.equals("I") && !type.equals("R")) {
                list.add(new Class(name, college, num, prof, location, crn, days, start, end, credits, type));
            }
            count = 1;
            name = college = num = prof = location = crn = start = end = type = null;
            days = null;
        }
        return list;
    }
}
