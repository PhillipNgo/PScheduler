package scheduler;

import time.Time;
import time.TimeException;

/**
 * Course represents a class at Virginia Tech
 * 
 * @author Phillip Ngo
 */
public class VTCourse {
    
    //Fields
    
    private String name; 
    private String subj; 
    private String num; 
    private String prof; 
    private String crn; //course request number
    private int credits; 
    
    //class type - Lecture (L), Lab (B), Recitation (C), Online (O), Independent Study(I), Empo (E), Research (R), Hyrbrid (H)
    private String type; 
    
    // Indeces of times, days, and locs, correspond to a main or additional time
    private LinkedList<Time> times;
    private LinkedList<String[]> days;
    private LinkedList<String> locs;
    
  //Constructors ------------------------------------------------------------------------
    /**
     * Constructor for a course
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
    public VTCourse(String crn, String subject, String number, String name, String type, int credits, 
                    String prof, LinkedList<String[]> days, LinkedList<Time> times, LinkedList<String> locs) throws TimeException {
        this.crn = crn;
        this.subj = subject;
        this.num = number;
        this.name = name;
        this.type = type;
        this.credits = credits;
        this.prof = prof;
        this.days = days;
        this.times = times;
        this.locs = locs;
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
    public LinkedList<String> getLocations() {
        return locs;
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
    public LinkedList<String[]> getDays() {
        return days;
    }
    
    /**
     * The TimeSlot data that holds the start and end times
     * 
     * @return time
     */
    public LinkedList<Time> getTimes() {
        return times;
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
    
    //Methods --------------------------------------------------------------------------
    
    /**
     * Checks if a Class has a time and day conflict with another Class
     * 
     * @param c the class to be compared
     * @return true if there is a time conflict
     */
    public boolean conflicts(VTCourse other) {
        if (other == null) {
            return false;
        }
        
        for (int i = 0; i < days.size(); i++) {
            for (int j = 0; j < other.getDays().size(); j++) {
                String[] thisDays = this.days.get(i);
                Time thisTime = this.times.get(i);
                
                String[] otherDays = other.days.get(j);
                Time otherTime = other.times.get(j);
                
                if (thisTime != null && otherTime != null) { //if either of the times are null, they can't conflict
                    for (String td : thisDays) {
                        for (String od : otherDays) {
                            if (td.equals(od) && thisTime.conflicts(otherTime)) {
                                return true;
                            }
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
    
    /**
     * A String representing course information
     * @return the crn, subject and number
     */
    @Override
    public String toString() {
        return crn + " - " + subj + " " + num; 
    }
}
