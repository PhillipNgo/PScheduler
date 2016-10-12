package scheduler;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * ScheduleMaker generates a list of schedules
 * can only be called in a static way
 * 
 * @author Phillip Ngo
 */
public final class ScheduleMaker {
    
    /**
     * Generates all possible schedules with the given parameter
     * 
     * @param term the term value
     * @param subjects array of subjects where the indices correspond to the indices of numbers
     * @param numbers array of numbers where the indices correspond to the indices of subjects
     * @param types array of class types where the indices correspond
     * @param onlineAllowed array of booleans noting whether online for a class is allowed
     * @param start the earliest time allowed
     * @param end the latest time allowed
     * @param freeDay freeDay if there is one
     * @param crns any specific crns
     * @return the LinkedList of all the schedules
     * @throws Exception
     */
    public static LinkedList<Schedule> generateSchedule(String term, LinkedList<String> subjects, LinkedList<String> numbers, LinkedList<String> types,   
                                                        String start, String end, String freeDay, LinkedList<String> crns) throws Exception {
        LinkedList<Schedule> schedules = new LinkedList<>();
        LinkedList<LinkedList<VTCourse>> classes = new LinkedList<>();
        HashMap<String, HashMap<String, LinkedList<VTCourse>>> map = null; 
        VTParser parser = null;
        try {
            //throw new FileNotFoundException(); //debugging
            map = VTParser.parseTermFile(term);
        }
        catch (FileNotFoundException e) {
            parser = new VTParser(term);
        }
        for (int i = 0; i < subjects.size(); i++) {
            LinkedList<VTCourse> curr;
            if (map != null) {
                curr = map.get(subjects.get(i)).get(numbers.get(i));
            }
            else {
                curr = parser.parseCourse(subjects.get(i), numbers.get(i));
            }
            
            Iterator<VTCourse> iter = curr.iterator();
            while (iter.hasNext()) {
                if (!checkRestrictions(iter.next(), start, end, types.get(i), freeDay)) {
                    iter.remove();
                }
            }
            if (curr.isEmpty()) {
                return schedules;
            }
            classes.add(curr);
        }
        
        if (crns.size() != 0) {
            parser = new VTParser(term);
            for (String crn : crns) {
                LinkedList<VTCourse> curr = new LinkedList<>();
                curr.add(parser.parseCRN(crn));
                classes.add(curr);
            }
        }
        
        createSchedules(classes, new Schedule(), 0, schedules);
     
        return schedules;
    }
    
   /**
    * Creates all valid schedules
    * 
    * @param classListings A list holding a list for each class
    * @param schedule the schedule to add to schedules
    * @param classIndex current index in classListings
    * @param schedules the list of possible schedules
    * @throws Exception
    */
    private static void createSchedules(LinkedList<LinkedList<VTCourse>> classListings, Schedule schedule,
                                        int classIndex, LinkedList<Schedule> schedules) throws Exception { 
        for (VTCourse course : classListings.get(classIndex)) {
            Schedule copy = schedule;
            if (classIndex == classListings.size() - 1) {
                copy = schedule.createCopy();
            }
            
            try {
                copy.add(course);
            }
            catch (TimeException e) {
                continue;
            }
            
            if (classIndex == classListings.size() - 1) {
                schedules.add(copy);
            }
            else {
                createSchedules(classListings, copy, classIndex + 1, schedules);
                schedule.remove(course);
            }
        }
    }
    
    /**
     * Checks if a course meets the given restrictions
     * 
     * @param course the class to check
     * @param start the start time restriction
     * @param end the end time restriction
     * @param freeDay the free day restriction
     * @return true if the course meets the restrictions
     * @throws TimeException
     */
    private static boolean checkRestrictions(VTCourse course, String start, String end, String type, String freeDay) throws TimeException {
        Time time = course.getTimeSlot();
         
        if (type != "A" && type != course.getClassType()) {
            return false;
        }
        
        if (time == null) {
            return true;
        }      
        int startTime = Time.timeNumber(start);
        int endTime = Time.timeNumber(end);
        if (Arrays.binarySearch(course.getDays(), freeDay) >= 0) {
            return false;
        }
        else if (time.getStartNum() < startTime || time.getEndNum() > endTime) {
            return false;
        }
        
        time = course.getAdditionalTime();
        if (time == null) {
            return true;
        }
        startTime = Time.timeNumber(start);
        endTime = Time.timeNumber(end);
        if (Arrays.binarySearch(course.getAdditionalDays(), freeDay) >= 0) {
            return false;
        }
        else if (time.getStartNum() < startTime || time.getEndNum() > endTime) {
            return false;
        }

        return true;
    }
    
    public static void main(String[] args) throws Exception {
        String term = "201701";
        String[] subjects = new String[]{"CS", "CS", "ECE", "ECE", "ECE", "ECE", "ECE"};
        String[] numbers = new String[]{"3114", "2506", "2014", "2524", "2704", "2204", "2274"};
        String[] types = new String[]{"L", "L", "L", "L", "L", "L", "B"};
        String[] crns = new String[]{};
        //String[] subjects = new String[]{"AHRM", "AHRM", "MKTG", "FIN", "MUS", "BIT"};
        //String[] numbers = new String[]{"2644", "2674", "3104H", "3074", "2056", "2406"};
        //String[] types = new String[]{"O", "L", "L", "L", "L", "L"};
        //String[] crns = new String[]{"10061"};
        //String[] subjects = new String[]{"MATH"};
        //String[] numbers = new String[]{"1014"};
        //String[] types = new String[]{"L"};
        //String[] crns = new String[]{};
        String start = "8:00AM";
        String end = "8:00PM";
        String freeDay = "";
        //long startTime = System.currentTimeMillis();
        LinkedList<Schedule> schedules = ScheduleMaker.generateSchedule(term, new LinkedList<>(subjects), new LinkedList<>(numbers), 
                                                                        new LinkedList<>(types), start, end, freeDay, new LinkedList<>(crns));
        //System.out.println(schedules + "\n" + schedules.size());
        //long endTime = System.currentTimeMillis();
        //System.out.println(endTime - startTime);
        if (schedules.size() == 0) {
            throw new TimeException("No possible schedules, there must be conflicts");
        }
        ExcelSchedule.outputFile(schedules);
    }
}
