package scheduler;

import time.Time;
import time.TimeException;

/**
 * Schedule represents a list of classes and a timetable that holds their times
 * 
 * @author Phillip Ngo
 */
public class Schedule extends LinkedList<VTCourse> {
    
    //Fields
    private int totalCredits;
    
    /**
     * DAYS contains the names of days from monday to friday
     */
    public static final String DAYS = "MTWRF";
    
    //Constructors
    /**
     * Default constructor, creates the LinkedList and sets credits to 0
     */
    public Schedule() {
        super();
        totalCredits = 0;
    }
    
    public Schedule(LinkedList<VTCourse> courses) throws Exception {
        this();
        for (VTCourse curr : courses) {
            this.add(curr);
        }
    }
    
    //Methods
    /**
     * Adds a course to the schedule
     * 
     * @param c the course to be added
     * @throws TimeException if the course conflicts with the schedule
     */
    @Override
    public void add(VTCourse c) throws Exception {
        if (c == null) {
            throw new IllegalArgumentException("Course is null when adding to schedule");
        }
        for (VTCourse curr : this) {
            if (curr.conflicts(c)) {
                throw new TimeException("Class conflicts with current schedule");
            }
        }
        totalCredits += c.getCredits();
        super.add(c);
    }
    
    /**
     * Removes the specified course from the schedule
     * 
     * @param c the course to be removed
     * @return the removed class
     */
    @Override
    public VTCourse remove(VTCourse c) {
        totalCredits -= c.getCredits();
        return super.remove(c);
    }
    
    /**
     * The amount of credits of the schedule
     * 
     * @return the sum of the credits
     */
    public int totalCredits() {
        return totalCredits;
    }
    
    /**
     * The earliest class start time of the schedule
     * 
     * @return the minute number of the earliest time
     */
    public int earliestTime() {
        int early = 1500;
        for (VTCourse curr : this) {
            int min = 1500;
            for (Time t : curr.getTimes()) {
                if (t != null) {
                    min = Math.min(t.getStartNum(), min);
                }
            }
            early = Math.min(min, early);
        }
        if (early == 1500) {
            return -1;
        }
        return early;
    }
    
    /**
     * The latest class start time of the schedule
     * 
     * @return the minute number of the latest time
     */
    public int lastestTime() {
        int late = -1;
        for (VTCourse curr : this) {
            int max = -1;
            for (Time t : curr.getTimes()) {
                if (t != null) {
                   max = Math.max(t.getEndNum(), max);
                }
            }
            late = Math.max(max, late);
        }
        return late;
    }
    
    /**
     * Checks if the schedule is busy at a minute time
     * @param day the day number
     * @param time the minute time of the day
     * @return true if its busy
     */
    public boolean isBusy(int day, int time) {
        for (VTCourse c : this) {
            for (int i = 0; i < c.getDays().size(); i++) {
                Time t = c.getTimes().get(i);
                String[] days = c.getDays().get(i);
                for (String d : days) {
                    if (day == DAYS.indexOf(d) && time >= t.getStartNum() && time <= t.getEndNum()-5) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Creates a copy schedule
     * 
     * @return a copy of the schedule
     */
    public Schedule createCopy() throws Exception {
        Schedule schedule = new Schedule();
        for (VTCourse curr : this) {
            schedule.add(curr);
        }
        return schedule;
    }
}
