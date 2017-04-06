package time;

/**
 * Time represents a slot of time defined by a start time and an end time.
 * Each start and end time is defined with an hour, minute, and AM/PM
 *
 * @author Phillip Ngo
 */
public class Time {

    //Fields

    private String start;
    private String end;

    //Constructors

    /**
     * Creates a Time object with a beginning and end time
     *
     * @param start the time the slot starts
     * @param end   the time the slot ends
     */
    public Time(String start, String end) throws TimeException {
        if (!isTime(start)) {
            throw new TimeException(start + " is an invalid time");
        }
        if (!isTime(end)) {
            throw new TimeException(end + " is an invalid time");
        }
        this.start = start;
        this.end = end;
    }
    
    /**
     * Creates a Time object with a beginning and end time
     *
     * @param start the minute time the slot starts
     * @param end   the minute time the slot ends
     */
    public Time(int start, int end) throws TimeException {
        if (start > end) {
            throw new TimeException(start + " starts after " + end);
        }
        
        if (start < 0 || start > 1440) {
            throw new TimeException(start + " is an invalid minute number");
        }
        
        if (end < 0 || end > 1440) {
            throw new TimeException(end + " is na invalid minute number");
        }
        this.start = timeString(start);
        this.end = timeString(end);
    }

    // Methods

    /**
     * The start time as a string in the format 10:30AM
     *
     * @return the start time
     */
    public String getStart() {
        return start;
    }

    /**
     * The end time as a string in the format 10:30AM
     *
     * @return the end time
     */
    public String getEnd() {
        return end;
    }

    /**
     * The start time as an int
     * Examples: 2:00AM = 120
     *
     * @return the start time
     */
    public int getStartNum() {
        return timeNumber(start);
    }

    /**
     * The end time as an int
     * Examples: 2:00AM = 120
     *
     * @return the start time
     */
    public int getEndNum() {
        return timeNumber(end);
    }

    /**
     * Compares Time slots to see if there is a time conflict
     *
     * @param other Time slot to be compared
     * @return true if there is a time conflict
     */
    public boolean conflicts(Time other) {
        if (other == null) {
            throw new IllegalArgumentException();
        }
        if ((other.getStartNum() >= this.getStartNum() && other.getStartNum() <= this.getEndNum()) ||
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
        } else if (other == this) {
            return true;
        } else {
            Time copy = (Time) other;
            return copy.getStart().equals(this.getStart()) && copy.getEnd().equals(this.getEnd());
        }
    }

    // Static Methods

    /**
     * Checks if the given string is a valid time
     * Valid times are given in the same format as: 8:30AM or 10:20PM
     *
     * @param time the string to be checked
     * @return True if the string is a valid time
     */
    public static boolean isTime(String time) {
        if (time == null) {
            return false;
        }
        String[] splitTime = time.split(":");
        if (splitTime.length != 2 || splitTime[0].length() > 2 ||
                splitTime[1].length() != 4) {
            return false;
        } else {
            try {
                Integer.parseInt(splitTime[0]);
            } catch (NumberFormatException e) {
                return false;
            }
            try {
                Integer.parseInt(splitTime[1].substring(0, 2));
            } catch (NumberFormatException e) {
                return false;
            }
            return splitTime[1].substring(2, 4).equalsIgnoreCase("PM") || splitTime[1].substring(2, 4).equalsIgnoreCase("AM");
        }
    }

    /**
     * Converts a time String into a minute value
     * For example: 1:00AM returns 60
     *
     * @param time the time to be parsed
     * @return the minute value of the time
     */
    public static int timeNumber(String time) {
        String[] t = time.split(":");
        int hour = Integer.parseInt(t[0]);
        if (t[1].substring(2, 4).equalsIgnoreCase("PM") && hour != 12) {
            hour += 12;
        } else if (t[1].substring(2, 4).equalsIgnoreCase("AM") && hour == 12) {
            hour = 0;
        }
        hour *= 60;
        int minute = Integer.parseInt(t[1].substring(0, 2));
        return hour + minute;
    }

    /**
     * Converts a minute value to a time in the String format 10:30AM
     *
     * @param time the number to be converted
     * @return the time String, null if the minute value is not a valid time number
     */
    public static String timeString(int time) throws TimeException {
        if (time < 0 || time > 1440) {
            throw new TimeException(time + " is an invalid minute number. Must be between 0 and 1440");
        }
        String suffix;
        if (time >= 720) {
            suffix = "PM";
        } else {
            suffix = "AM";
        }

        int hours = time / 60;
        int mins = time - hours * 60;

        if (suffix.equals("PM") && hours != 12) {
            hours -= 12;
        } else if (suffix.equals("AM") && hours == 0) {
            hours += 12;
        }

        if (mins < 10) {
            return hours + ":0" + mins + suffix;
        } else {
            return hours + ":" + mins + suffix;
        }
    }
}
