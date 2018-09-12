package schedule;

/**
 * Timetable represents a time block of X amount of days split into 5 minute intervals from 8am - 7pm
 * 
 * @author Phillip Ngo
 * @version 1.0
 */
public class Timetable {
    
    //Fields -----------------------------------------------------------------------------

    private boolean[][] timetable; //array that holds the time in intervals of 5 minutes
    /**
     * DAYS contains the names of days from monday to friday
     */
    public static final String[] DAYS = new String[] 
    {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

    //Constructors -----------------------------------------------------------------------
    
    /**
     * Default Constructor creates a Timetable of 5 days with 24 hour time spans split
     * into 5 minute intervals
     */
    public Timetable() {
        timetable = new boolean[5][24*12];
    }
    
    //Private Methods ---------------------------------------------------------------------
    
    /**
     * Turns a string day to its equivalent index
     * value in the array DAYS
     * 
     * @param day string of the day
     * @return number equivalent
     */
    public static int dayToNum(String day) {
        if (day == null) {
            return -1;
        }
        else if (day.equalsIgnoreCase("monday") || day.equals("M")) {
            return 0;
        }
        else if (day.equalsIgnoreCase("tuesday") || day.equals("T") ) {
            return 1;
        }
        else if (day.equalsIgnoreCase("wednesday") || day.equals("W"))  {
            return 2;
        }
        else if (day.equalsIgnoreCase("thursday") || day.equals("R")) {
            return 3;
        }
        else if (day.equalsIgnoreCase("friday") || day.equals("F")) {
            return 4;
        }
        else
            return -1;
    }
    
    //Methods ----------------------------------------------------------------------------
    
    /**
     * Checks if a string contains a valid day
     * 
     * @param day string to be checked
     * @return true if the string is a day
     */
    public static boolean isDay(String day) {
        if (day == null) {
            return false;
        }
        if (day.equalsIgnoreCase("monday") || day.equals("M") ||
            day.equalsIgnoreCase("tuesday") || day.equals("T") ||
            day.equalsIgnoreCase("wednesday") || day.equals("W") ||
            day.equalsIgnoreCase("thursday") || day.equals("R") || 
            day.equalsIgnoreCase("friday") || day.equals("F")) {
            return true;
        }
        return false;
    }
    
    /**
     * Makes the specified days and times "busy" by marking array positions as true
     * 
     * @param days days that the event happens
     * @param time time during the given day it is busy
     */
    public void addEvent(String[] days, TimeSlot time) {
        if (days == null || time == null) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < days.length; i++) {
            if (dayToNum(days[i]) < 0) {
                throw new IllegalArgumentException();
            }
        }
        for (int d = 0; d < days.length; d++) {
            for (int t = time.getStartNum()/5; t < time.getEndNum()/5; t++) {
                timetable[dayToNum(days[d])][t] = true;
            }
        }
    }
    
    /**
     * Clears all events in the given day during the specified time slot
     * 
     * @param days days of the event
     * @param time time of the event
     */
    public void clearEvents(String[] days, TimeSlot time) {
        if (days == null || time == null) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < days.length; i++) {
            if (dayToNum(days[i]) < 0) {
                throw new IllegalArgumentException();
            }
        }
        for (int d = 0; d < days.length; d++) {
            for (int t = time.getStartNum()/5; t < time.getEndNum()/5; t++) {
                timetable[dayToNum(days[d])][t] = false;
            }
        }
    }
    
    /**
     * Clears all events on the entire schedule with the specified time
     * 
     * @param time to clear
     */
    public void clearEvents(TimeSlot time) {
        if (time == null) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < timetable.length; i++) {
            for (int j = time.getStartNum()/5; j < time.getEndNum()/5; j++) {
                timetable[i][j] = false;
            }
        }
    }
    
    /**
     * Clears all events on the specified day
     * 
     * @param day day to clear
     */
    public void clearEvents(String day) {
        if (dayToNum(day) < 0) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < timetable[dayToNum(day)].length; i++) {
            timetable[dayToNum(day)][i] = false;
        }

    }
    
    /**
     * Clears entire schedule
     */
    public void clear() {
        for (int r = 0; r < timetable.length; r++) {
            for (int c = 0; c < timetable[r].length; c++) {
                timetable[r][c] = false;
            }
        }
    }
    
    /**
     * The earliest time an event begins
     * 
     * @return string containing the time the event starts
     */
    public String earliestTime() {
        int min = timetable[0].length;
        for (int r = 0; r < timetable.length; r++) {
            for (int c = 0; c < timetable[r].length; c++) {
                if (timetable[r][c] == true && c < min) { 
                    min = c;
                }
            }
        }
        if (min == timetable[0].length) {
            return null;
        }
        else {
            return TimeSlot.num2Time(min*5);
        }     
    }
    
    /**
     * The latest ending event time
     * 
     * @return string containing the time the event ends
     */
    public String latestTime() {
        int max = -1;
        for (int r = 0; r < timetable.length; r++) {
            for (int c = 0; c < timetable[r].length; c++) {
                if (timetable[r][c] == true && c > max) { 
                    max = c;
                }
            }
        }
        if (max == -1) {
            return null;
        }
        else {
            return TimeSlot.num2Time((max + 1)*5);
        }   
    }
}
