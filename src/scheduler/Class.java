package scheduler;

/**
 * Class represents a course at Virginia Tech
 * 
 * @author Phillip Ngo
 * @version 1.0
 */
public class Class {

    //Fields -----------------------------------------------------------------------------
    
    private String name; 
    private String college; 
    private String num; 
    private String prof; 
    private String location; 
    private String crn; 
    private String[] days; 
    private TimeSlot time; 
    private int credits; 
    private String classType;
    
    private String[] addDays;
    private TimeSlot addTime;
    private String addLocation;
    
    //Constructors ------------------------------------------------------------------------
    /**
     * Constructor for a specific Class 
     * 
     * @param name name of class (Ex: Intro to Programming)
     * @param college abbreviation of what college it is in (Ex: CS)
     * @param num the number that is associated with the class (Ex: 1114 in 'CS 1114')
     * @param prof name of the professor
     * @param loc building and room number
     * @param crn unique code for specific class
     * @param days what days the class meets
     * @param start what time the class starts 
     * @param end what time the class ends
     * @param credits how many credits the class is
     * @param type (Ex: Lab, Recitation, Lecture)
     */
    public Class(String name, String college, String num, String prof, String loc, String crn, 
                 String[] days, String start, String end, int credits, String type) {
        if (name == null || college == null || num == null || prof == null || loc == null ||
            crn == null || days == null || start == null || end == null || type == null) {
            throw new IllegalArgumentException("cannot create class with null");
        }
        if (!TimeSlot.isTime(start) || !TimeSlot.isTime(end)) {
            throw new IllegalArgumentException("not a time");
        }
        for (int i = 0; i < days.length; i++) {
            if (!Timetable.isDay(days[i])) {
                throw new IllegalArgumentException("not a day");
            }
        }
        this.name = name;
        this.college = college;
        this.num = num;
        this.prof = prof;
        this.location = loc;
        this.crn = crn;
        this.days = days;
        this.time = new TimeSlot(start, end);
        this.credits = credits;
        this.classType = type;
        this.addDays = null;
        this.addTime = null;
    }
    
    /**
     * Constructor for a general Class
     * 
     * @param college
     * @param num
     */
    public Class(String college, String num) {
        if (college == null || num == null) {
            throw new IllegalArgumentException();
        }
        this.name = "";
        this.college = college;
        this.num = num;
        this.prof = "";
        this.location = "";
        this.crn = "";
        this.time = null;
        this.credits = 0;
        this.classType = "";
        this.addDays = null;
        this.addTime = null;
    }

    //Getter Methods ------------------------------------------------------------------------
    
    /**
     * The Class Name
     * 
     * @return class name
     */
    public String getName() {
        return name;
    }
    
    /**
     * The college abbreviation the class is in
     * 
     * @return the college
     */
    public String getCollege() {
        return college;
    }

    /**
     * Class number associated with the college
     * 
     * @return class number
     */
    public String getNum() {
        return num;
    }

    /**
     * The name of the professor teaching the class
     * 
     * @return name of professor
     */
    public String getProf() {
        return prof;
    }
    
    /**
     * The building and room number the Class is in
     * 
     * @return building and room number
     */
    public String getLocation() {
        return location;
    }
    
    /**
     * The unique code for specific classes
     * 
     * @return the crn
     */
    public String getCRN() {
        return crn;
    }

    /**
     * The days the class meets
     * 
     * @return array with day names
     */
    public String[] getDays() {
        return days;
    }
    
    /**
     * The TimeSlot data that holds the start and end times
     * 
     * @return timeslot
     */
    public TimeSlot getTimeSlot() {
        return time;
    }
    
    /**
     * The start time of the Class
     * 
     * @return start time
     */
    public String getStartTime() {
        return time.getStart();
    }

    /**
     * The time the Class ends
     * 
     * @return end time
     */
    public String getEndTime() {
        return time.getEnd();
    }

    /**
     * The length in minutes of the class
     * 
     * @return end time - start time
     */
    public int classLength() {
        return time.getEndNum() - time.getStartNum();
    }
    
    /**
     * The amount of credits the Class is worth
     * 
     * @return credits
     */
    public int getCredits() {
        return credits;
    }
    
    /**
     * The type of class it is
     * 
     * @return classType
     */
    public String getClassType() {
        return classType;
    }
    
    /**
     * The additional days that have different times
     * 
     * @return addDays
     */
    public String[] getAdditionalDays() {
        return addDays;
    }
    
    /**
     * he additional time for the additional days
     * 
     * @return addtime
     */
    public TimeSlot getAdditionalTime() {
        return addTime;
    }
    
    /**
     * The location for the additional days
     * 
     * @return addLocation;
     */
    public String getAdditionalLocation() {
        return addLocation;
    }
    
    //Setter Methods ---------------------------------------------------------------------
    
    /**
     * Add additional days that have different times
     * 
     * @param days the days to be added
     */
    public void setAdditionalDays(String[] days) {
        for (int i = 0; i < days.length; i++) {
            if (!Timetable.isDay(days[i])) {
                throw new IllegalArgumentException();
            }
        }
        addDays = days;
    }
    
    /**
     * Add additional time for the additional days
     * 
     * @param time the TimeSlot to be added
     */
    public void setAdditionalTime(TimeSlot time) {
        if (!TimeSlot.isTime(time.getStart()) || !TimeSlot.isTime(time.getEnd())) {
            throw new IllegalArgumentException();
        }
        addTime = time;
    }
    
    /**
     * Add the location for the additional days
     * 
     * @param location the location to be added
     */
    public void setAdditionalLocation(String location) {
        addLocation = location;
    }
    
    //Methods --------------------------------------------------------------------------
    
    /**
     * Checks if a Class has a time conflict with another Class
     * 
     * @param c the class to be compared
     * @return true if there is a time conflict
     */
    public boolean conflicts(Class other) {
        if (other == null) {
            return false;
        }
        
        String[] otherDays = other.getDays();
        for (int i = 0; i < otherDays.length; i++) {
            for (int j = 0; j < days.length; j++) {
                if (otherDays[i].equals(days[j]) && time.conflicts(other.getTimeSlot())) {
                    return true;
                }
            }
            if (addDays != null && addTime != null && addLocation != null) { 
                for (int k = 0; k < addDays.length; k++) {
                    if (otherDays[i].equals(addDays[k]) && addTime.conflicts(other.getTimeSlot())) {
                        return true;
                    }
                }
            }
        }
        if (other.getAdditionalDays()!= null && other.getAdditionalTime() != null && other.getAdditionalLocation() != null) {
            String[] otherAddDays = other.getAdditionalDays();
            for (int i = 0; i < otherAddDays.length; i++) {
                for (int j = 0; j < days.length; j++) {
                    if (otherAddDays[i].equals(days[j]) && time.conflicts(other.getAdditionalTime())) {
                        return true;
                    }
                }
                if (addDays != null && addTime != null && addLocation != null) { 
                    for (int k = 0; k < addDays.length; k++) {
                        if (otherAddDays[i].equals(addDays[k]) && addTime.conflicts(other.getAdditionalTime())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Checks if a Class has a time conflict with a Schedule
     * 
     * @param s schedule to be compared
     * @return true if there is a time conflict
     */
    public boolean conflicts(Schedule other) {
        if (other == null) {
            throw new IllegalArgumentException();
        }
        LinkedList<Class> schedule = other.getClasses();
        for (int i = 0; i < schedule.size(); i++) {
            if (schedule.get(i).conflicts(this)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if an object is equal to Class
     * Classes are Equal if the CRN, class number, and college are the same
     * 
     * @param other the object to be compared
     * @return true if the object is equal
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
            Class copy = (Class) other;
            return copy.getCRN().equals(this.getCRN()) && 
                   copy.getNum().equals(this.num) && 
                   copy.getCollege().equals(this.getCollege());
        }
    }
    
    /**
     * Checks if an object is equal to Class
     * Classes are Equal if class number, and college are the same
     * 
     * @param other the object to be compared
     * @return true if the object is equal
     */
    public boolean generalEquals(Object other) {
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        else if (other == this) {
            return true;
        }
        else {
            Class copy = (Class) other;
            return copy.getNum().equals(this.num) && 
                   copy.getCollege().equals(this.getCollege());
        }
    }
    

    /**
     * Prints the object information 
     * 
     * @return the list of credentials for Class
     */
    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < days.length; i++) {
            s += days[i];
        }
        int[] maxLengths = new int[]{5, 10, 30, 1, 1, 20, 3, 17, 10};
        String[] toAdd = new String[]{crn, college + " " + num, name, classType, credits + "", prof, s, 
                                      time.getStart() + " - " + time.getEnd(), location};
        s = "";
        for (int i = 0; i < maxLengths.length; i++) {
            s += toAdd[i];
            String pad = addSpaces(toAdd[i], maxLengths[i]);
            s += pad;
            if (i != maxLengths.length - 1){
                s += "   ";
            }
        }
        
        if (addDays != null && addTime != null && addLocation != null) {
            s += "<br>" + addSpaces("", 118);
            String addedDays = "";
            for (int i = 0; i < addDays.length; i++) {
                addedDays += addDays[i];
            }
            s += addedDays + addSpaces(addedDays, 3) + "   ";
            s += addTime.getStart() + " - " + addTime.getEnd() + addSpaces(addTime.getStart() + " - " + time.getEnd(), 17);
            s += "   " + addLocation + addSpaces(addLocation, 10);
        }
        return s;
    }
    
    /**
     * Appends Spaces to a string
     * 
     * @param str the string to have spaces added
     * @param length the length the string should be
     * @return string with added spaces until it meets the length
     */
    private String addSpaces(String str, int length) {
        String spaces = "";
        for (int i = 0; i < length-str.length(); i++) {
            spaces += " ";
        }
        return spaces;
    }
}
