package scheduler;

/**
 * TimeSlot represents a time block with a start and end time
 * 
 * @author Phillip Ngo
 * @version 1.0
 */
public class TimeSlot {
    
    //Fields -----------------------------------------------------------------------------
    
    private String startTime;
    private String endTime;
    
    //Constructors -----------------------------------------------------------------------
    
    /**
     * TimeSlot constructor that takes a start time and an end time
     * 
     * @param start time the event starts
     * @param end time the event ends
     */
    public TimeSlot(String start, String end) {
        if (!isTime(start) || !isTime(end)) {
            throw new IllegalArgumentException();
        }
        startTime = start;
        endTime = end;
    }
    
    //Private Methods ---------------------------------------------------------------------
    
    /**
     * Changes a time to a its corresponding minute value
     * 
     * 
     * @param time time to be parsed
     * @return equivalent number in time
     */
    public static int parseTime(String time) { 
        String[] t = time.split(":");
        int hour = Integer.parseInt(t[0]);
        if (t[1].substring(2, 4).equalsIgnoreCase("PM") && hour != 12) {
            hour += 12;
        }
        else if (t[1].substring(2, 4).equalsIgnoreCase("AM") && hour == 12) {
            hour = 0;
        }
        hour *= 60;
        int minute = Integer.parseInt(t[1].substring(0, 2));
        return hour + minute;
    }
    
    //Getter Methods ----------------------------------------------------------------------
    
    /**
     * The start time
     * 
     * @return the start time
     */
    public String getStart() {
        return startTime;
    }
    
    /**
     * The end time
     * 
     * @return the end time
     */
    public String getEnd() {
        return endTime;
    }
    
    /**
     * The start time to a minute number
     * 
     * @return the start time
     */
    public int getStartNum() {
        return parseTime(startTime);
    }
    
    /**
     * The end time to a minute number
     * 
     * @return the start time
     */
    public int getEndNum() {
        return parseTime(endTime);
    }
    
    //Setter Methods ----------------------------------------------------------------------
    
    /**
     * Sets the start time to the specified time
     * 
     * @param start time to be changed to
     */
    public void setStart(String start) {
        if (!isTime(start)) {
            throw new IllegalArgumentException();
        }
        startTime = start;
    }
    
    /**
     * Sets the end time to the specified time
     * 
     * @param end time to be changed to
     */
    public void setEnd(String end) {
        if (!isTime(end)) {
            throw new IllegalArgumentException();
        }
        endTime = end;
    }
    
    //Methods ----------------------------------------------------------------------------

    /**
     * Changes a number to a time in minutes
     * For example: 1:00AM would be 60, 2:00AM = 120, 12:00PM = 12*12
     * 
     * @param num number to be parsed
     * @return equivalent time 
     */
    public static String num2Time(int num) {
        if (num < 0 || num > 24*60) {
            throw new IllegalArgumentException();
        }
        String suffix;
        if (num >= 720) {
            suffix = "PM";
        }
        else {
            suffix = "AM";
        }
            
        int hours = num/60;
        int mins = num - hours*60;
        
        if (suffix.equals("PM") && hours != 12) {
            hours -= 12;
        }
        else if (suffix.equals("AM") && hours == 0) {
            hours += 12;
        }
        
        if (mins < 10) {
            return hours + ":0" + mins + suffix;
        }
        else {
            return hours + ":" + mins + suffix;
        }
    }
    
    /**
     * Checks if a string contains a valid time
     * Form: HH:MM (Example 8:30AM)
     * 
     * @param time the string to be checked
     * @return 
     */
    public static boolean isTime(String time) {
        if (time == null) {
            return false;
        }
        String[] splitTime = time.split(":");
        if (splitTime.length != 2 || splitTime[0].length() > 2 || 
                splitTime[1].length() != 4) {
            return false;
        }
        else {
            try {
                Integer.parseInt(splitTime[0]);
            }
            catch (NumberFormatException e) {
                return false;
            }
            try {
                Integer.parseInt(splitTime[1].substring(0, 2));
            }
            catch (NumberFormatException e) {
                return false;
            }
            return true;
        }
    }
    
    /**
     * The length in minutes of the time slot
     * 
     * @return length of the time
     */
    public int length() {
        return parseTime(endTime) - parseTime(startTime);
    }
    
    /**
     * Compares Time slots to see if there is a time conflict
     * 
     * @param other Time slot to be compared
     * @return true if there is a time conflict
     */
    public boolean conflicts(TimeSlot other) {
        if (other == null) {
            throw new IllegalArgumentException();
        }
        if((other.getStartNum() >= this.getStartNum() && other.getStartNum() <= this.getEndNum()) ||
            (other.getEndNum() >= this.getStartNum() && other.getEndNum() <= this.getEndNum()) ||
            (other.getStartNum() >= this.getStartNum() && other.getEndNum() <= this.getEndNum()) ||
            (other.getStartNum() <= this.getStartNum() && other.getEndNum() >= this.getEndNum())) {
            return true;
        }
        return false;
    }
    
    /**
     * Compares and object to a time slot to see if they are equal
     * two time slots are equal if their end times and start times are the same
     * 
     * @param other object to be compared
     * @return true if the objects are equal
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
            TimeSlot copy = (TimeSlot) other;
            return copy.getStart().equals(this.getStart()) && copy.getEnd().equals(this.getEnd());
        }
    }
    
    public static void main(String[] args) {
        System.out.println(parseTime("1:10PM"));
    }
}
