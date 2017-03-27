package scheduler;

import time.Time;
import time.TimeException;

/**
 * Course represents a college course at Virginia Tech
 * 
 * @author Phillip Ngo
 */
public class VTCourse {
    
    //Fields
    
    private String name; 
    private String subj; 
    private String num; 
    private String prof; 
    private String loc; //class location 
    private String crn; //course request number
    private String[] days; 
    private Time time; 
    private int credits; 
    
    //class type - Lecture (L), Lab (B), Recitation (C), Online (O), Independent Study(I), Empo (E), Research (R), Hyrbrid (H)
    private String type; 
    
    
    //if the class has an additional time slot
    private String[] addDays;
    private Time addTime;
    private String addLoc;
    
  //Constructors ------------------------------------------------------------------------
    /**
     * Constructor for a class with a day and time
     * 
     * @param name name of class (Ex: Intro to Programming)
     * @param subject abbreviation of what college it is in (Ex: CS)
     * @param num the number that is associated with the class (Ex: 1114 in 'CS 1114')
     * @param prof name of the professor
     * @param loc building and room number
     * @param crn unique code for specific class
     * @param days what days the class meets
     * @param start what time the class starts 
     * @param end what time the class ends
     * @param credits how many credits the class is
     * @param type (Ex: Lab, Recitation, Lecture)
     * @throws TimeException when an invalid time is inputed
     */
    public VTCourse(String name, String subject, String num, String prof, String loc, String crn, 
                 String[] days, String start, String end, int credits, String type) throws TimeException {
        if (name == null || subject == null || num == null || prof == null || loc == null ||
            crn == null || days == null || start == null || end == null || type == null) {
            throw new IllegalArgumentException("One or more fields are null when creating a course");
        }
        if (credits < 0 || credits > 19) {
            throw new IllegalArgumentException("Course with " + credits + " credits might be wrong");
        }
        if (!Time.isTime(start) && !Time.isTime(end)) {
            throw new TimeException("Inputted an invalid time when creating a course");
        }
        if (!type.equals("O") && !type.equals("R") && !type.equals("I") && !type.equals("E") && 
                !type.equals("L") && !type.equals("B") && !type.equals("C") && !type.equals("H")) {
                throw new IllegalArgumentException("The course with " + type + " used the online constructor");
        }
        for (int i = 0; i < days.length; i++) {
            if (!isDay(days[i])) {
                throw new IllegalArgumentException("not a day");
            }
        }
        
        this.name = name;
        this.subj = subject;
        this.num = num;
        this.prof = prof;
        this.loc = loc;
        this.crn = crn;
        this.days = days;
        this.time = new Time(start, end);
        this.credits = credits;
        this.type = type;
        this.addDays = null;
        this.addTime = null;
    }
    
    /**
     * Constructor for a Class with an additional time
     * 
     * @param name name of class (Ex: Intro to Programming)
     * @param subject abbreviation of what college it is in (Ex: CS)
     * @param num the number that is associated with the class (Ex: 1114 in 'CS 1114')
     * @param prof name of the professor
     * @param loc building and room number
     * @param crn unique code for specific class
     * @param days what days the class meets
     * @param start what time the class starts 
     * @param end what time the class ends
     * @param credits how many credits the class is
     * @param type (Ex: Lab, Recitation, Lecture)
     * @param additionalDays an additional day slot
     * @param additionalStart an additional start time slot
     * @param additionalEnd an additional end time slot
     * @param additionalLoc an additional location
     * @throws TimeException when an invalid time is inputed
     */
    public VTCourse(String name, String subject, String num, String prof, String loc, String crn, 
                 String[] days, String start, String end, int credits, String type, String[] additionalDays,
                 String additionalStart, String additionalEnd, String additionalLoc) throws TimeException {
        this(name, subject, num, prof, loc, crn, days, start, end, credits, type);
        if (additionalDays == null || additionalStart == null || additionalEnd == null || additionalLoc == null) {
            throw new IllegalArgumentException("One or more fields are null when creating a course");
        }
        if (!Time.isTime(additionalStart) || !Time.isTime(additionalEnd)) {
            throw new TimeException("Inputted an invalid time when creating a course");
        }
        
        for (int i = 0; i < additionalDays.length; i++) {
            if (!isDay(additionalDays[i])) {
                throw new IllegalArgumentException(days[i] + " is not a day");
            }
        }
        
        this.addDays = additionalDays;
        this.addTime = new Time(additionalStart, additionalEnd);
        this.addLoc = additionalLoc;
    }
    
    /**
     * Constructor for a course without a day or time assigned
     * 
     * @param name name of class (Ex: Intro to Programming)
     * @param subject abbreviation of what college it is in (Ex: CS)
     * @param num the number that is associated with the class (Ex: 1114 in 'CS 1114')
     * @param prof name of the professor
     * @param crn unique code for specific class
     * @param credits how many credits the class is
     * @param type (Ex: Lab, Recitation, Lecture)
     */
    public VTCourse(String name, String subject, String num, String prof, String crn, int credits, String type) {
        if (!type.equals("O") && !type.equals("R") && !type.equals("I") && !type.equals("E") && 
            !type.equals("L") && !type.equals("B") && !type.equals("C") && !type.equals("H")) {
            throw new IllegalArgumentException("The course with " + type + " used the no-time constructor");
        }
        if (name == null || subject == null || num == null || prof == null || crn == null || type == null) {
                throw new IllegalArgumentException("One or more fields are null when creating a course");
        }
        if (credits < 0 || credits > 19) {
            throw new IllegalArgumentException("Course with " + credits + " credits might be wrong");
        }
        this.name = name;
        this.subj = subject;
        this.num = num;
        this.prof = prof;
        this.loc = null;
        this.crn = crn;
        this.days = null;
        this.time = null;
        this.credits = credits;
        this.type = type;
        this.addDays = null;
        this.addTime = null;
    }
    
    //Private Methods
    /**
     * Checks if a string contains a valid day
     * 
     * @param day string to be checked
     * @return true if the string is a day
     */
    private boolean isDay(String day) {
        if (day == null) {
            return false;
        }
        if (day.equalsIgnoreCase("monday") || day.equals("M") ||
            day.equalsIgnoreCase("tuesday") || day.equals("T") ||
            day.equalsIgnoreCase("wednesday") || day.equals("W") ||
            day.equalsIgnoreCase("thursday") || day.equals("R") || 
            day.equalsIgnoreCase("friday") || day.equals("F") ||
            day.equalsIgnoreCase("saturday") || day.equals("S") ||
            day.equalsIgnoreCase("sunday") || day.equals("U")){
            return true;
        }
        return false;
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
    public String getSubject() {
        return subj;
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
        return loc;
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
     * @return time
     */
    public Time getTimeSlot() {
        return time;
    }

    /**
     * The length in minutes of the class
     * 
     * @return end time - start time
     */
    public int classLength() {
        if (time == null) {
            return -1;
        }
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
        return type;
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
     * @return addTime
     */
    public Time getAdditionalTime() {
        return addTime;
    }
    
    /**
     * The location for the additional days
     * 
     * @return addLocation;
     */
    public String getAdditionalLocation() {
        return addLoc;
    }
    
    //Methods --------------------------------------------------------------------------
    
    /**
     * Checks if a Class has a time conflict with another Class
     * 
     * @param c the class to be compared
     * @return true if there is a time conflict
     */
    public boolean conflicts(VTCourse other) {
        if (other == null) {
            return false;
        }
        else if (time == null || other.getTimeSlot() == null) {
            return false;
        }
        
        String[] otherDays = other.getDays();
        for (int i = 0; i < otherDays.length; i++) {
            for (int j = 0; j < days.length; j++) {
                if (otherDays[i].equals(days[j]) && time.conflicts(other.getTimeSlot())) {
                    return true;
                }
            }
            if (addDays != null && addTime != null && addLoc != null) { 
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
                if (addDays != null && addTime != null && addLoc != null) { 
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
            VTCourse copy = (VTCourse) other;
            return copy.getCRN().equals(this.getCRN()) && 
                   copy.getNum().equals(this.num) && 
                   copy.getSubject().equals(this.getSubject());
        }
    }
    
    public String toString() {
        return crn + " - " + subj + " " + num; 
    }
}
