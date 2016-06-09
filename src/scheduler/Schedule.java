package scheduler;

/**
 * Schedule represents a list of classes and a timetable that holds their times
 * 
 * @author Phillip Ngo
 * @version 1.0
 */
public class Schedule {
    
    //Fields -----------------------------------------------------------------------------
    
    private Timetable timetable;
    private LinkedList<Class> classes; 

    //Constructors -----------------------------------------------------------------------
    
    /**
     * Default Constructor for Schedule. Instantiates objects.
     */
    public Schedule() {
        timetable = new Timetable();
        classes = new LinkedList<Class>();
    }
    
    /**
     * Constructor that takes an array of classes and adds them to the Schedule
     * 
     * @param classes list of classes to add to Schedule
     */
    public Schedule(LinkedList<Class> classes) {
        this();
        if (classes == null) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i) == null) {
                throw new IllegalArgumentException();
            }
            addClass(classes.get(i));
        }     
    }

    //Getter Methods ----------------------------------------------------------------------
    
    /**
     * The classes in the Schedule
     * 
     * @return array of classes in schedule
     */
    public LinkedList<Class> getClasses() {
        return classes.createCopy();
    }

    /**
     * The amount of classes in the Schedule
     * 
     * @return number of classes
     */
    public int getNumClasses() {
        return classes.size();
    }

    //Methods ----------------------------------------------------------------------------
    
    /**
     * Adds the specified Class into the Schedule
     * 
     * @param c the Class to be added
     */
    public void addClass(Class c) {
        if (c == null) {
            throw new IllegalArgumentException();
        }
        classes.add(c);
        timetable.addEvent(c.getDays(), c.getTimeSlot());
        if (c.getAdditionalDays() != null && c.getAdditionalTime() != null && c.getAdditionalLocation() != null) {
            timetable.addEvent(c.getAdditionalDays(), c.getAdditionalTime());
        }
    }

    /**
     * Removes the specified Class from the Schedule
     * 
     * @param c class to be removed
     */
    public void removeClass(Class c) {
        if (c == null) {
            throw new IllegalArgumentException();
        }
        classes.remove(c);
        timetable.clearEvents(c.getDays(), c.getTimeSlot());
        if (c.getAdditionalDays() != null && c.getAdditionalTime() != null && c.getAdditionalLocation() != null) {
            timetable.clearEvents(c.getAdditionalDays(), c.getAdditionalTime());
        }
    }

    /**
     * The timetable element
     * 
     * @return timetable
     */
    public Timetable getTimetable() {
        return timetable;
    }
    
    /**
     * The corresponding class that has the given day and start time
     * 
     * @param day the day specified
     * @param startTime the time specified
     * @return the class if it exists
     */
    public String getHTMLSchedule(int day, String startTime) {
        StringBuilder html = new StringBuilder();
        for (int i = 0; i < classes.size(); i++) {
            Class currClass = classes.get(i);
            String[] days = currClass.getDays();
            for (int j = 0; j < days.length; j++) {
                if (Timetable.dayToNum(days[j]) == day) {
                    if (currClass.getStartTime().equals(startTime)) {
                        html.append(currClass.getCollege() + " " + currClass.getNum() + "<br>");
                        html.append("CRN: " + currClass.getCRN() + "<br>");
                        html.append(currClass.getStartTime() + " - " + currClass.getEndTime() + "<br>");
                        html.append(currClass.getLocation() + "<br>");
                        html.append("Prof: " + currClass.getProf());
                        int start = TimeSlot.parseTime(currClass.getStartTime())/5;
                        int end = TimeSlot.parseTime(currClass.getEndTime())/5;
                        html.append("--" + (end-start));
                        return html.toString();
                    }
                    break;
                }
            }
            if (currClass.getAdditionalDays() != null) {
                days = currClass.getAdditionalDays();
                for (int j = 0; j < days.length; j++) {
                    if (Timetable.dayToNum(days[j]) == day) {
                        if (currClass.getAdditionalTime().getStart().equals(startTime)) {
                            html.append(currClass.getCollege() + " " + currClass.getNum() + "<br>");
                            html.append("CRN: " + currClass.getCRN() + "<br>");
                            html.append(currClass.getAdditionalTime().getStart() + " - " + currClass.getAdditionalTime().getEnd() + "<br>");
                            html.append(currClass.getAdditionalLocation() + "<br>");
                            html.append("Prof: " + currClass.getProf());
                            int start = TimeSlot.parseTime(currClass.getAdditionalTime().getStart())/5;
                            int end = TimeSlot.parseTime(currClass.getAdditionalTime().getEnd())/5;
                            html.append("--" + (end-start));
                            return html.toString();
                        }
                        break;
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * The Class with the earliest starting time
     * 
     * @return Class that starts first
     */
    public Class getEarliestClass() {
        Class c = classes.get(0);
        if (c == null) {
            return null;
        }
        int min = classes.get(0).getTimeSlot().getStartNum();
        for (int i = 1; i < classes.size(); i++) {
            if (classes.get(i).getTimeSlot().getStartNum() < min) {
                min = classes.get(i).getTimeSlot().getStartNum();
                c = classes.get(i);
            }
            if (c.getAdditionalDays() != null && c.getAdditionalTime() != null && c.getAdditionalLocation() != null) {
                if (classes.get(i).getAdditionalTime().getStartNum() < min) {
                    min = classes.get(i).getTimeSlot().getStartNum();
                    c = classes.get(i);
                }
            }
        }
        
        return c;
    }
    
    /**
     * The Class with the latest ending time
     * 
     * @return Class that ends last
     */
    public Class getLatestClass() {
        Class c = classes.get(0);
        if (c == null) {
            return null;
        }
        int max = classes.get(0).getTimeSlot().getEndNum();
        for (int i = 1; i < classes.size(); i++) {
            if (classes.get(i).getTimeSlot().getEndNum() > max) {
                max = classes.get(i).getTimeSlot().getEndNum();
                c = classes.get(i);
            }
            if (c.getAdditionalDays() != null && c.getAdditionalTime() != null && c.getAdditionalLocation() != null) {
                if (classes.get(i).getAdditionalTime().getEndNum() > max) {
                    max = classes.get(i).getTimeSlot().getEndNum();
                    c = classes.get(i);
                }
            }
        }
        return c;
    }

    /**
     * Total amount of credits in the Schedule
     * 
     * @return sum of all credits in class list
     */
    public int getTotalCredits() {
        int sum = 0;
        for (int i = 0; i < classes.size(); i++) {
            sum += classes.get(i).getCredits();
        }
        return sum;
    }

    /**
     * Removes all Classes from the Schedule
     */
    public void clear() {
        timetable.clear();
        classes.clear();
    }

    /**
     * Checks class similarities between schedules
     * 
     * @param other schedule to be checked against
     * @return array of the same class CRNs if there are any
     */
    public LinkedList<String> compareSchedule(Schedule other) {
        if (other == null) {
            return null;
        }
        LinkedList<Class> list = other.getClasses();
        LinkedList<String> crns = new LinkedList<String>();
        
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < this.getNumClasses(); j++) {
                if (list.get(i).equals(classes.get(j))) {
                    crns.add(list.get(i).getCRN());
                }
            }
        }
        
        return crns;
    }

    /**
     * Checks if an object is equal to Schedule
     * Schedules are Equal if the there are the same amount of classes 
     * and each contain the same classes
     * 
     * @param other object to be checked against
     * @return true if they are equal
     */
    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        else if (other == this) {
            return true;
        }
        else {
            Schedule copy = (Schedule) other;
            if (copy.getNumClasses() != this.getNumClasses()) {
                return false;
            }
            LinkedList<Class >list = copy.getClasses();
            for (int i = 0; i < list.size(); i++) {
                if (classes.contains(list.get(i)) < 0) {
                    return false;
                }
            }
            return true;
        }      
    }
    
    /**
     * Prints the object information
     * 
     * @return the list of classes and their credentials
     */
    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < this.getNumClasses(); i++) {
            s += classes.get(i).toString() + "<br>";
        }
        return s;
    }
}
