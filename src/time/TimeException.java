package time;

@SuppressWarnings("serial")
/**
 * Represents a problem when dealing with time conflicts or creating time
 * @author Phillip
 */
public class TimeException extends Exception {
    
    /**
     * Create a time exception
     * @param message
     */
    public TimeException(String message) {
        super(message);
    }
}
