package scheduler;

import java.io.FileNotFoundException;
import java.util.HashMap;

import parser.VTParser;
import time.Time;
import time.TimeException;

/**
 * ScheduleMaker generates a list of schedules based on given restrictions
 * 
 * @author Phillip Ngo
 */
public class ScheduleMaker {
    
    private LinkedList<Schedule> schedules; //the final list that holds the schedules
    private HashMap<String, LinkedList<VTCourse>> listings; //holds the courses that were found on the time table
    private HashMap<String, LinkedList<VTCourse>> pass; //holds the courses from the timtable that also passed the restrictions
    private HashMap<String, LinkedList<VTCourse>> fail; //holds the courses from the timetable that did not pass the restrictions
    private LinkedList<VTCourse> crnCourses; //holds the courses that were specifically requested by crn
    public static final int MAX_SCHEDULES = 200; //sets a limit to how many schedules can be generated
    
    /**
     * Constructor creates all schedule combinations and compiles data when created
     * @param term the term to parse
     * @param subjects subjects of the classes. index corresponds to numbers, types, and profs
     * @param numbers numbers of the classes. index corresponds to subjects, types, and profs
     * @param types types of the classes. index corresponds to subjects, numbers, and profs
     * @param start start time restriction
     * @param end end time restriction
     * @param freeDays free days restriction
     * @param crns the crns to add
     * @param profs the prof preferences of the classes. index corresponds to subjetcs, numbers, and types
     * @throws Exception
     */
    public ScheduleMaker(String term, LinkedList<String> subjects, LinkedList<String> numbers, LinkedList<String> types,   
            String start, String end, String[] freeDays, LinkedList<String> crns, LinkedList<String> profs) throws Exception {
        listings = new HashMap<>();
        pass = new HashMap<>();
        fail = new HashMap<>();
        crnCourses = new LinkedList<>();
        schedules = generateSchedule(term, subjects, numbers, types, start, end, freeDays, crns, profs);
    }
    
    /**
     * List of possible schedules
     * @return schedules
     */
    public LinkedList<Schedule> getSchedules() {
        return schedules;
    }
    
    /**
     * Listings found for the classes inputed
     * @return listings
     */
    public HashMap<String, LinkedList<VTCourse>> getListings() {
        return listings;
    }
    
    /**
     * The listings of specific crns
     * @return crnCourses
     */
    public LinkedList<VTCourse> getCrns() {
        return crnCourses;
    }
    
    /**
     * The classes that failed the restrictions sorted by course numbers
     * @return fail
     */
    public HashMap<String, LinkedList<VTCourse>> getFailed() {
        return fail;
    }
    
    /**
     * The classes that passed the restrictions sorted by course numbers
     * @return pass
     */
    public HashMap<String, LinkedList<VTCourse>> getPassed() {
        return pass;
    }
    
    /**
     * Generates all possible schedules with the given parameter
     * Also stores data about the courses within the listings, pass, and fail field members as it generates
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
    private LinkedList<Schedule> generateSchedule(String term, LinkedList<String> subjects, LinkedList<String> numbers, LinkedList<String> types,   
                                                        String start, String end, String[] freeDays, LinkedList<String> crns, LinkedList<String> profs) throws Exception {
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
            LinkedList<VTCourse> curr = null;
            String type = types.get(i);
            String subj = subjects.get(i);
            String num = numbers.get(i);
            try {
                if (map != null) {
                    LinkedList<VTCourse> find = map.get(subj).get(num);
                    listings.put(type + subj + " " + num, find.createCopy());
                    curr = find.createCopy();
                }
                else {
                    LinkedList<VTCourse> find = parser.parseCourse(subj, num);
                    listings.put(type + subj + " " + num, find);
                    curr = find.createCopy();
                }
            }
            catch (Exception e) {
                throw new Exception(subj + " " + num + " does not exist on the timetable");
            }
            LinkedList<VTCourse> failed = new LinkedList<>();
            for (VTCourse c : curr) {
                if (!checkRestrictions(c, start, end, type, freeDays, profs.get(i))) {
                    curr.remove(c);
                    if (!c.getClassType().equals(type)) {
                        listings.get(type + subj + " " + num).remove(c);
                    }
                    else {
                        failed.add(c);
                    }
                }
            }
            pass.put(types.get(i) + subjects.get(i) + " " + numbers.get(i), curr);
            fail.put(types.get(i) + subjects.get(i) + " " + numbers.get(i), failed);
            classes.add(curr);
        }
        
        if (crns.size() != 0) {
            for (String crn : crns) {
                LinkedList<VTCourse> curr = new LinkedList<>();
                outerloop:
                for (String subj : map.keySet()) {
                    HashMap<String, LinkedList<VTCourse>> subject = map.get(subj);
                    for (String numb : subject.keySet()) {
                        LinkedList<VTCourse> list = subject.get(numb);
                        for (VTCourse c : list) {
                            if (crn.equals(c.getCRN())) {
                                curr.add(c);
                                crnCourses.add(c);
                                for (VTCourse c2 : list) {
                                    if (!c2.getClassType().equals(c.getClassType())) {
                                        list.remove(c2);
                                    }
                                }
                                listings.put(c.getClassType() + subj + " " + numb, list);
                                break outerloop;
                            }
                        }
                    }
                }
                classes.add(curr);
            }
        }
        try {
            createSchedules(classes, new Schedule(), 0, schedules);
        } catch (Exception e) {}
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
    private void createSchedules(LinkedList<LinkedList<VTCourse>> classListings, Schedule schedule,
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
                // If there are too many schedules, exits the stack by throwing an exception
                if (schedules.size() >= MAX_SCHEDULES) { 
                    throw new Exception("Too Many Schedules");
                }
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
    private boolean checkRestrictions(VTCourse course, String start, String end, String type, String[] freeDays,
                                             String prof) throws TimeException {
        
        if (!type.equals("A") && !type.equals(course.getClassType())) {
            return false;
        }
        
        if (!prof.equals("A") && !(prof).equals(course.getProf().replace("-", " "))) {
            return false;
        }
        
        for (int i = 0; i < course.getTimes().size(); i++) {
            Time time = course.getTimes().get(i);
            String[] days = course.getDays().get(i);
            
            if (time != null) {
                if (freeDays != null) {
                    for (String d : freeDays) {
                        for (String d2 : days) {
                            if (d.equals(d2)) {
                                return false;
                            }
                        }
                    }
                }
                
                int startTime = Time.timeNumber(start);
                int endTime = Time.timeNumber(end);
                if (time.getStartNum() < startTime || time.getEndNum() > endTime) {
                    return false;
                }
            }
        }
        
        return true;
    }
}
