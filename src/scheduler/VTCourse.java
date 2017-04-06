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
    private int capacity;
    
    //class type - Lecture (L), Lab (B), Recitation (C), Online (O), Independent Study(I), Empo (E), Research (R), Hyrbrid (H)
    private String type; 
    
    // Indeces of times, days, and locs, correspond to a main or additional time
    private LinkedList<Time> times;
    private LinkedList<String[]> days;
    private LinkedList<String> locs;
    
    //Holds the exam code
    private String exam;
    
  //Constructors ------------------------------------------------------------------------
    /**
     * Constructor for a course
     * @param crn The course request number
     * @param subject the college the course is within (CS, PHYS, MKTG)
     * @param number the course number (2114, 1114, 1000)
     * @param name the name of the course
     * @param type the types of course (L (Lecture), O (Online), ...)
     * @param credits the amount of credits
     * @param capacity the capacity of the course
     * @param prof the professor
     * @param days list of days with indices corresponding to the times and locs
     * @param times list of times with indices corresponding to the days and locs
     * @param locs the list of locations with indices corresponding to the days and times
     * @param exam the exam time and location code
     * @throws TimeException
     */
    public VTCourse(String crn, String subject, String number, String name, String type, int credits, int capacity, 
                    String prof, LinkedList<String[]> days, LinkedList<Time> times, LinkedList<String> locs, String exam)
                            throws TimeException {
        this.crn = crn;
        this.subj = subject;
        this.num = number;
        this.name = name;
        this.type = type;
        this.credits = credits;
        this.capacity = capacity;
        this.prof = prof;
        this.days = days;
        this.times = times;
        this.locs = locs;
        this.exam = exam;
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
     * The max amount of people in a class
     * 
     * @return capacity
     */
    public int getCapacity() {
        return capacity;
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
     * The exam code
     * 
     * @return exam
     */
    public String getExam() {
        return exam;
    }
    
    //Methods --------------------------------------------------------------------------
    
    /**
     * Checks if a Class has a time and day conflict with another Class
     * 
     * @param c the class to be compared
     * @return true if there is a time conflict
     * @throws TimeException 
     */
    public boolean conflicts(VTCourse other) throws TimeException {
        return conflicts(other, 0);
    }
    
    /**
     * Checks if a Class has a time and day conflict with another Class
     * 
     * @param c the class to be compared
     * @param minGap the minimum break time between the classes
     * @return true if there is a time conflict
     * @throws TimeException 
     */
    public boolean conflicts(VTCourse other, int minGap) throws TimeException {
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
                            if (td.equals(od)) {
                                Time gapTime = new Time(thisTime.getStartNum() - minGap + 1, thisTime.getEndNum() + minGap - 1);
                                if (gapTime.conflicts(otherTime)) {
                                    return true;
                                }
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
