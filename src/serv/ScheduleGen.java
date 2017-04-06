package serv;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import scheduler.LinkedList;
import scheduler.Schedule;
import scheduler.ScheduleMaker;
import scheduler.VTCourse;
import time.Time;
import time.TimeException;

/**
 * Servlet implementation class ScheduleGen
 * Creates the dynamic page of schedule search results
 */
@WebServlet("/ScheduleGen")
public class ScheduleGen extends HttpServlet {
    private static final long serialVersionUID = 1L;
    /**
     * The color coding for schedules
     */
    private static String[] colors = new String[]{"orange", "lightseagreen", "antiquewhite", "gold", "lightskyblue", 
                                                  "lightsalmon", "lightgreen", "lightblue", "lightyellow", 
                                                  "silver", "peachpuff", "pink"};
    private ScheduleMaker generator; //used to create all the schedules
    private String start; //the start time
    private String end; //the end time
    private String free; 
    
    //These represent the criteria searched for by the users, each index corresponds to a different criteria
    private LinkedList<String> subjects; 
    private LinkedList<String> numbers;
    private LinkedList<String> types;
    private LinkedList<String> crns;
    private LinkedList<String> profs;
    
    private StringBuilder html; //stores the html being generated
    private StringBuilder fullConflicts; //stores criteria in html if there were no possible schedules
                                               
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScheduleGen() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Set up response and generate schedules
        response.setContentType("text/html");
        html = new StringBuilder();
        PrintWriter printer = response.getWriter();
        LinkedList<Schedule> schedules;
        
        try {
            schedules = getSchedules(request); //create the schedules
        }
        catch (Exception e) {
            schedules = null; //something went wrong when generating schedules
        }
        fullConflicts = new StringBuilder();
        String restrictions = ""; //holds html for search restrictions
        String stats = ""; //holds html for search statistics
        String conflicts = ""; //holds html for search conflicts
        String courseSearch = ""; //holds html for the courses found on the timetable
        if (schedules != null) { //if there were no problems generating schedules
            restrictions = this.getRestrictions(); //get the restrictions
            stats = this.getScheduleStats(); //get the stats
            conflicts = "This feature has been temporarily disabled."; //large schedule amounts slowed down the website
            try {
                //conflicts = this.getConflicts(); //get the conflicts
            }
            catch (Exception e) {}
            courseSearch = this.getCourseSearch(); //get the course search
        }
        
        
        // -- HEAD START -- // Imports, favicon, scripts
        html.append("<!DOCTYPE html>");
        html.append("<html lang='en'>");
        html.append("<head>");
        html.append("<title>PScheduler | search</title>");
        html.append("<link rel='shortcut icon' href='favicon.ico' type='image/icon'>");
        html.append("<link rel='icon' href='favicon.ico' type='image/icon'>");
        html.append("<meta charset='utf-8'>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1'>");
        html.append("<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css'>");
        html.append("<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js'></script>");
        html.append("<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js'></script>");
        html.append("<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/css/bootstrap-select.min.css'>");
        html.append("<script src='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/js/bootstrap-select.min.js'></script>");
        html.append("<link rel='stylesheet' href='stylesheets/styles.css'>");
        html.append("<script type='text/javascript' src='javascripts/Schedules.js'");
        html.append("<script>");
        html.append("(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){");
        html.append("(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),");
        html.append("m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)");
        html.append("})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');");
        html.append("ga('create', 'UA-86032292-1', 'auto');");
        html.append("ga('send', 'pageview');");
        html.append("</script>");
        html.append("<link href='https://fonts.googleapis.com/css?family=Raleway|Roboto' rel='stylesheet'>");
        html.append("</head>");
        // -- HEAD END -- //

        
        // -- HEADER START -- // header banner
        html.append("<body style='background-color: #FFFAFA'>");
        html.append("<div id='header' style='background-color: DarkSlateGray; border-bottom: 1px solid darkorange;'>");
        html.append("<div class='container-fluid header'>");
        html.append("<h1>");
        html.append("<a href='http://www.pscheduler.com' style='text-decoration:none'>");
        html.append("<span style='color: darkorange'>P</span><span style='color: white'>Scheduler</span></a>");
        html.append("<small style='color: darkorange'> Virginia Tech Schedule Creation</small>");
        html.append("</h1>");
        html.append("</div>");
        html.append("</div>");
        // -- HEADER END -- //
        
        // -- BODY START -- // schedules and search data
        //create info and buttons bar
        html.append("<div id='content' class='container-fluid'>");
        html.append("<div style='padding-top: 10px;' class='row'>");
        html.append("<div style='padding-left: 12.5px; padding-right: 12.5px;' class='col-sm-12'>");
        html.append("<div id='schedule-panel' class=' collapse in panel panel-default outline'>");
        html.append("<div style='background-color: white;' class='panel-heading center'>");
        html.append("<div class='row'>");
        html.append("<div class='col-sm-5'>");
        html.append("<div class='input-group'>");
        html.append("<div class='row'>");
        html.append("<div style='padding:0 0 0 5px;' class='col-sm-4'>");
        html.append("<span class='input-group-btn'>");
        html.append("<button type='button' class='btn btn-default glyphicon glyphicon-chevron-left form-control' onclick='changeSchedule(-1)'></button>");
        html.append("</span>");
        html.append("</div>");
        html.append("<div style='padding:1px 0 0 0;' class='col-sm-4'>");
        html.append("<input id='changet' class='form-control' type='text' value='try keyboard arrows'> ");
        html.append("</div>");
        html.append("<div style='padding:0 3px 0 2px;' class='col-sm-4'>");
        html.append("<span class='input-group-btn'>");
        html.append("<button type='button' class='btn btn-default glyphicon glyphicon-chevron-right form-control' onclick='changeSchedule(1)'></button>");
        html.append("</span>");
        html.append("</div>");
        html.append("</div>");
        html.append("</div>");
        html.append("</div>");
        html.append("<div class='col-sm-2'>");
        
        html.append("<h4 id='title'><b>");
        if (schedules == null || schedules.size() == 0) {
            html.append("0 Schedules");
        }
        else if (schedules.size() > 200) {
            html.append("Schedule 1 of 200+");
        }
        else {
            html.append("Schedule 1 of " + schedules.size());
        }
        html.append("</b></h4>");
        
        html.append("</div>");
        html.append("<div style='text-align:right' role='group' class='btn-group col-sm-5' aria-label='...'>");
        
        //Set an invisible select for the modify form button
        html.append("<form action='modify' style='padding-right:12px'>");
        html.append("<button type='button' class='btn btn-default' onclick='printerFriendly()'>Print Page</button>");
        html.append("<a class='btn btn-default' id='download' href='download' onclick='dlHref()'>Download as Excel</a>");
        html.append("<button type='button' class='btn btn-default hidetb'>Hide Table</button>");
        html.append("<button type='submit' class='btn btn-default'>Modify Search</button>");
        html.append("<select style='display: none;' name='classes'><option value='" + request.getParameter("schedule") + "'></option></select>");
        html.append("<select style='display: none;' name='term'><option value='" + request.getParameter("term") + "'></option></select>");
        html.append("<select style='display: none;' name='h1'><option value='" + request.getParameter("h1") + "'></option></select>");
        html.append("<select style='display: none;' name='m1'><option value='" + request.getParameter("m1") + "'></option></select>");
        html.append("<select style='display: none;' name='start'><option value='" + request.getParameter("start") + "'></option></select>");
        html.append("<select style='display: none;' name='h2'><option value='" + request.getParameter("h2") + "'></option></select>");
        html.append("<select style='display: none;' name='m2'><option value='" + request.getParameter("m2") + "'></option></select>");
        html.append("<select style='display: none;' name='end'><option value='" + request.getParameter("end") + "'></option></select>");
        html.append("<select style='display: none;' name='gap'><option value='" + request.getParameter("gap") + "'></option></select>");
        String[] freeDays = request.getParameterValues("free");
        if (freeDays != null) {
            for (String d : freeDays) {
                html.append("<select style='display: none;' name='free'><option value='" + d + "'></option></select>");
            }
        }
        html.append("<button type='button' class='btn btn-default' onclick='togglePanel()'>Search Data</button>");
        html.append("</form>");
        html.append("</div>");
        html.append("</div>");
        html.append("</div>");
        
        //Append the schedules
        html.append("<div id='tablebody' style='padding-top:0px' class='panel-body'>");
        try {
            if (schedules == null) {
                html.append("Sorry, something went wrong when trying to generate your schedules.");
            }
            else if (schedules.size() == 0) {
                html.append("No Schedules Matched Your Parameters. See below or 'Search Data' for more information. This may be caused by classes always conflicting.<br><br>");
                
                html.append("<div class='row'>");
                html.append("<div class='col-sm-4'>");
                html.append("<p><b>Your Restrictions</b><p>");
                html.append(restrictions);
                html.append("</div>");
                html.append("</div><br>");
                html.append("<p><b>Courses Found on Timetable</b><p>");
                html.append(courseSearch.toString());
            }
            else {
                html.append("<ul class='collapse in' style='padding-left:0' id='textschedules' name='0'>");
                appendTextSchedules(schedules);
                html.append("</ul>");
           
                html.append("<ul style='padding-top:5px; padding-left:0' id='tableschedules' name='0'>");
                appendTableSchedules(schedules);
                html.append("</ul>");
            }
        }
        catch (Exception e) {
            html.append("There was an error when displaying your schedules!<br>");
        }
        
        html.append("</div>");
        html.append("</div>");
        //search data starts here
        html.append("<div id='data-panel' class='collapse panel panel-default outline'>");
        html.append("<div style='background-color: white;' class='panel-heading center'>");
        html.append("<div class='row'>");
        html.append("<div class='col-sm-1'>");
        html.append("</div>");
        html.append("<div class='center col-sm-10'>");
        html.append("<h4><b>Search Data</b></h4>");
        html.append("</div>");
        html.append("<div class='col-sm-1'>");
        html.append("<button type='button' class='btn btn-default' onclick='togglePanel()'>Schedules</button>");
        html.append("</div>");
        html.append("</div>");
        html.append("</div>");
        
        html.append("<div class='panel-body panel-group'>");
        
        //The restrictions 
        html.append("<div style='padding-bottom:10px;' class='row'>");
        html.append("<div class='col-sm-5'>");
        html.append("<div class='panel panel-default'>");
        html.append("<div data-toggle='collapse' href='#collapseOne' class='panel-heading' role='button'>");
        html.append("<h3 class='panel-title'><b>Your Restrictions</b></h3>");
        html.append("</div>");
        html.append("<div id='collapseOne' class='panel-collapse collapse in' role='tabpanel'>");
        html.append("<div class='panel-body'>");
        html.append(restrictions);
        html.append("</div>");
        html.append("</div>");
        html.append("</div>");
        html.append("</div>");
        
        //The stats
        html.append("<div style='padding:0 0 0 0;' class='col-sm-2'>");
        html.append("<div class='panel panel-default'>");
        html.append("<div data-toggle='collapse' href='#collapseFour' class='panel-heading' role='button'>");
        html.append("<h3 class='panel-title'><b>Search Statistics</b></h3>");
        html.append("</div>");
        html.append("<div id='collapseFour' class='panel-collapse collapse in' role='tabpanel'>");
        html.append("<div class='panel-body'>");
        html.append(stats);
        html.append("</div>");
        html.append("</div>");
        html.append("</div>");
        html.append("</div>");
        
        //The conflicts
        html.append("<div class='col-sm-5'>");
        html.append("<div class='panel panel-default'>");
        html.append("<div data-toggle='collapse' href='#collapseThree' class='panel-heading' role='button'>");
        html.append("<h3 class='panel-title'><b>Conflicts</b></h3>");
        html.append("</div>");
        html.append("<div id='collapseThree' class='panel-collapse collapse in' role='tabpanel'>");
        html.append("<div class='panel-body'>");
        html.append(conflicts);
        html.append("</div>");
        html.append("</div>");
        html.append("</div>");
        html.append("</div>");
        html.append("</div>");
        
        //The course search
        html.append("<div class='panel panel-default'>");
        html.append("<div data-toggle='collapse' href='#collapseTwo' class='panel-heading' role='button'>");
        html.append("<h3 class='panel-title'><b>Courses Found on Timetable</b></h3>");
        html.append("</div>");
        html.append("<div id='collapseTwo' class='panel-collapse collapse in' role='tabpanel'>");
        html.append("<div class='panel-body'>");
        html.append(courseSearch);
        html.append("</div>");
        html.append("</div>");
        html.append("</div>");
        
        html.append("</div>");
        html.append("</div>");
        //search data end
        
        html.append("</div>");
        html.append("</div>");
        html.append("</div>");
        // -- BODY END -- //
        

        html.append("</body>");
        // -- FOOTER START -- //
        // -- FOOTER END -- //
        html.append("</html>");
        
        printer.print(html.toString());
        printer.flush();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
    
    /**
     * Returns the generated schedules and instantiates the generator in order to pull data
     * @param request The Request to get the data parameters from completed form
     * @return LinkedList of valid schedules
     * @throws Exception
     */
    private LinkedList<Schedule> getSchedules(HttpServletRequest request) throws Exception {
        start = request.getParameter("h1") + ":" + request.getParameter("m1") + request.getParameter("start");
        end = request.getParameter("h2") + ":" + request.getParameter("m2") + request.getParameter("end");
     
        
        if (!Time.isTime(start) || !Time.isTime(end)) {
            throw new TimeException("");
        }
        
        String[] freeDays = request.getParameterValues("free");
        free = "";
        if (freeDays != null) {
            for (String day : freeDays) {
                if (!Schedule.DAYS.contains(day)) {
                    throw new IllegalArgumentException();
                }
                free += day;
            }
        }
        else {
            free = "None";
        }
        
        String term = request.getParameter("term");
        String[] classes = request.getParameter("schedule").split("~");
        int minGap = Integer.parseInt(request.getParameter("gap"));
        subjects = new LinkedList<>();
        numbers = new LinkedList<>();
        types = new LinkedList<>();
        crns = new LinkedList<>();
        profs = new LinkedList<>();

        for (int i = 0; i < classes.length; i++) {
            if (classes[i].length() == 5) {
                crns.add(classes[i]);
            }
            else {
                String[] split = classes[i].split("-");
                int index = 0;
                if (split[0].charAt(split[0].length()-1) == 'H') {
                    index = 1;
                }
                types.add(split[0].substring(0, 1));
                subjects.add(split[0].substring(1, split[0].length()-4-index));
                numbers.add(split[0].substring(split[0].length()-4-index, split[0].length()));
                profs.add(split[1].replace("_", " "));
            }
        }

        generator = new ScheduleMaker(term, subjects, numbers, types, start, end, freeDays, minGap, crns, profs);
        return generator.getSchedules();
    }
    
    /**
     * Returns long form of a class type
     * @param type the short form
     * @return the corresponding long form
     */
    private String classType(String type) {
        switch (type) {
            case "L": return "Lecture";
            case "B": return "Lab";
            case "C": return "Recitation";
            case "H": return "Hybrid";
            case "E": return "Emporium";
            case "O": return "Online";
            case "I": return "Independent Study";
            case "R": return "Research";
            case "A": return "Any";
            default:  return "bug";
        }
    }
    
    /**
     * returns a string for a row or two of an html table that signifies a class
     * 
     * @param c the class to create
     * @param color if there will be color
     * @param colorInd the index of the color
     * @return html string for the class
     */
    private String textClass(VTCourse c, boolean color, int colorInd) {
        boolean hasTime = false;
        for (int i = 0; i < c.getTimes().size() && hasTime == false; i++) {
            if (c.getTimes().get(i) != null) {
                hasTime = true;
            }
        }
        StringBuilder html = new StringBuilder("<tr class='left'");
        if (color && hasTime) {
            html.append(" style='background-color:" + colors[colorInd] + " !important;'");
        }
        html.append("><td class='text'>" + c.getCRN() + "</td>");
        html.append("<td class='text'>" + c.getSubject() + " " + c.getNum() + "</td>");
        html.append("<td class='text pad'>" + c.getName() + "</td>");
        html.append("<td class='text'>" + classType(c.getClassType()) + "</td>");
        html.append("<td class='text'>" + c.getCredits() + "</td>");
        html.append("<td class='text'>" + c.getProf() + "</td>");       
        
        for (int i = 0; i < c.getTimes().size(); i++) {
            if (i != 0) {
                html.append("<tr ");
                if (color && hasTime) {
                    html.append("style='background-color:" + colors[colorInd] + " !important;'");
                }
                html.append("><td></td><td></td><td></td><td></td><td></td><td></td>");
            }
            
            Time t = c.getTimes().get(i);
            String[] days = c.getDays().get(i);
            if (t != null) {
                html.append("<td class='text'>");
                for (String d : days) {
                    html.append(d);
                }
                html.append("</td><td  class='text'>" + t.getStart() + " - " + t.getEnd() + "</td>");
                html.append("<td class='text pad'>" + c.getLocations().get(i) + "</td>");
            } else {
                html.append("<td class='text'>N/A</td>");
                html.append("<td class='text'>N/A</td>");
                html.append("<td class='text pad'>N/A</td>");
                html.append("</tr>");
            }
            html.append("</tr>");
        }
        
        return html.toString();
    }
    
    
    /**
     * Creates HTML schedule statistics like total credits and permutations 
     */
    private String getScheduleStats() {
        HashMap<String, LinkedList<VTCourse>> list = generator.getListings();
        StringBuilder html = new StringBuilder();
        int perm = 1;
        for (String key : list.keySet()) {
            perm *= list.get(key).size();
        }
        html.append("<b>Total # of Permutations: </b>" + perm + "<br>");
        list = generator.getPassed();
        perm = 1;
        for (String key : list.keySet()) {
            perm *= list.get(key).size();
        }
        html.append("<b># Passing the Restrictions: </b>" + perm + "<br>");
        LinkedList<Schedule> scheds = generator.getSchedules();
        html.append("<b># Possible (no conflicts):</b> ");
        if (generator.getSchedules().size() > 500) {
            html.append(">");
        }
        html.append(generator.getSchedules().size() + "<br>");
        if (scheds.size() == 0) {
            html.append("<b>Total Credits:</b> N/A");
        }
        else {
            html.append("<b>Total Credits:</b> " + generator.getSchedules().get(0).totalCredits());
        }
        return html.toString();
    }
    
    /**
     * Creates HTML representing the search restrictions 
     */
    private String getRestrictions() {
        StringBuilder html = new StringBuilder();
        html.append("<div class='row'><div class='col-sm-4'>");
        html.append("<b>Earliest Start:</b> " + start + "<br><b>Latest End:</b> " + end + "<br><b>Free Days:</b> " + free + "<br>");
        html.append("<b>CRNs: </b>");
        if (crns.size() == 0) {
            html.append("None");
        }
        for (String crn : crns) {
            html.append(crn);
            if (crns.indexOf(crn) < crns.size() - 1) {
                html.append(", ");
            }
        }
        html.append("</div><div class='col-sm-8'>");
        html.append("<b>Courses (course number / class type / professor):</b><br>");
        if (subjects.size() == 0) {
            html.append("None");
        }
        for (int i = 0; i < subjects.size(); i++) {
            html.append(subjects.get(i) + " " + numbers.get(i) + " / " + classType(types.get(i)) + " / ");
            if (!profs.get(i).equals("A")) {
                html.append(profs.get(i));
            }
            else {
                html.append("Any");
            }
            html.append("<br>");
        }
        html.append("</div></div>");
        return html.toString();
    }
    
    /**
     * Appends all class data found from the timetable. Data is found in the generator
     */
    private String getCourseSearch() {
        StringBuilder html = new StringBuilder();
        HashMap<String, LinkedList<VTCourse>> passed = generator.getPassed();
        HashMap<String, LinkedList<VTCourse>> failed = generator.getFailed();
        int i = 0;
        for (VTCourse c : generator.getCrns()) {
            String str = c.getClassType() + c.getSubject() + " " + c.getNum();
            LinkedList<VTCourse> listings = generator.getListings().get(str);
            html.append("<b>" + c.getSubject() + " " + c.getNum() + " - " + c.getName() + "</b><i> " + classType(c.getClassType()) + "</i> | " + listings.size() + " Sections<br>");
            html.append("<a class='btn btn-default glyphicon glyphicon-plus' onclick='changeIcon(this)' role='button' data-toggle='collapse' href='#pass" + i +"'>");
            html.append("</a> CRN " + c.getCRN() + "<br>");
            html.append("<div class='collapse' id='pass" + i + "'>");
            html.append("<table class='table text'>");
            html.append(textClass(c, false, -1));
            html.append("</table>");
            html.append("</div>");
            listings.remove(c);
            html.append("<a class='btn btn-default glyphicon glyphicon-plus' onclick='changeIcon(this)' role='button' data-toggle='collapse' href='#fail" + i + "'>");
            html.append("</a> " + listings.size() +  " other sections<br>");
            html.append("<div class='collapse' id='fail" + i++ + "'>");
            html.append("<table class='table text'>");
            for (VTCourse co : listings) {
                html.append(textClass(co, false, -1));
            }
            html.append("</table>");
            html.append("</div>");
        }
        for (String str : passed.keySet()) {
            LinkedList<VTCourse> listings = generator.getListings().get(str);
            html.append("<b>" + str.substring(1) + " - " + listings.get(0).getName() + "</b><i> " + classType(listings.get(0).getClassType()) + "</i> | "+ listings.size() + " Sections<br>");
            html.append("<a class='btn btn-default glyphicon glyphicon-plus' onclick='changeIcon(this)' role='button' data-toggle='collapse' href='#pass" + i +"'>");
            html.append("</a> " + passed.get(str).size() + " met the restrictions<br>");
            html.append("<div class='collapse' id='pass" + i + "'>");
            html.append("<table class='table text'>");
            for (VTCourse c : passed.get(str)) {
                html.append(textClass(c, false, -1));
            }
            html.append("</table>");
            html.append("</div>");
            
            html.append("<a class='btn btn-default glyphicon glyphicon-plus' onclick='changeIcon(this)' role='button' data-toggle='collapse' href='#fail" + i + "'>");
            html.append("</a> " + failed.get(str).size() +  " did not meet the restrictions<br>");
            html.append("<div class='collapse' id='fail" + i++ + "'>");
            html.append("<table class='table text'>");
            for (VTCourse c : failed.get(str)) {
                html.append(textClass(c, false, -1));
            }
            html.append("</table>");
            html.append("</div>");
            
            if (passed.get(str).size() == 0) {
                fullConflicts.append("<b>" + str.substring(1) + " - " + listings.get(0).getName() + "</b><i> " + classType(listings.get(0).getClassType()) + "</i> | "+ listings.size() + " Sections<br>");
                fullConflicts.append("<a class='btn btn-default glyphicon glyphicon-plus' onclick='changeIcon(this)' role='button' data-toggle='collapse' href='#fail" + i + "'>");
                fullConflicts.append("</a> " + failed.get(str).size() +  " did not meet the restrictions<br>");
                fullConflicts.append("<div class='collapse' id='fail" + i++ + "'>");
                fullConflicts.append("<table class='table text'>");
                for (VTCourse c : failed.get(str)) {
                    fullConflicts.append(textClass(c, false, -1));
                }
                fullConflicts.append("</table>");
                fullConflicts.append("</div>");
            }
        }
        return html.toString();
    }
    
    /**
     * Creates html tables for a list of schedule, all but the first are hidden
     * @param schedules the schedules to create
     */
    private void appendTextSchedules(LinkedList<Schedule> schedules) {
        int i = 0;
        for (Schedule schedule : schedules) {
            if (i != 0) {
                html.append("<table id='" + (i++) + "' style='display: none;' class='text table table-condensed'>");
            }
            else {
                html.append("<table id='" + (i++) + "' class='text table table table-condensed'>");
            }
            html.append("<tr style='font-weight:bold'><td class='text'>CRN</td> <td  class='text'>Course</td> <td  class='text'>Title</td>"
                    + "<td class='text'>Type</td><td  class='text'>Credits (" + schedules.get(0).totalCredits() + ")</td><td  class='text'>Instructor</td>"
                    + "<td class='text'>Days</td> <td  class='text'>Time</td> <td  class='text'>Location</td></tr>");
            int j = 0;
            for (VTCourse c : schedule) {
                html.append(textClass(c, true, j));
                j++;
            }
            html.append("</table>");
        }      
    }
    
    /**
     * Appends the visualized tables to the html, the all but the first are hidden
     * @param schedules the schedues
     * @throws TimeException
     */
    private void appendTableSchedules(LinkedList<Schedule> schedules) throws TimeException {
        int i = 0;
        for (Schedule schedule : schedules) {
            if (i != 0) {
                html.append("<table class='outline' id='" + (i++) + "' style='display:none;text-align:center;'>");
            }
            else {
                html.append("<table class='outline' id='" + (i++) + "' style='text-align:center;'>");
            }
            
            html.append("<tr>");
            html.append("<td class='center' style='width:100pt; height:35pt;'>" + "</td>");
            html.append("<td class='outline center' style='width:200pt; height:35pt;'>Monday</td>");
            html.append("<td class='outline center' style='width:200pt; height:35pt;'>Tuesday</td>");
            html.append("<td class='outline center' style='width:200pt; height:35pt;'>Wednesday</td>");
            html.append("<td class='outline center' style='width:200pt; height:35pt;'>Thursday</td>");
            html.append("<td class='outline center' style='width:200pt; height:35pt;'>Friday</td>");
            html.append("</tr>");
            
            int early = schedule.earliestTime()/60;
            int late = schedule.lastestTime()/60;
            for (int time = 0; time < late - early + 1; time++) {
                for (int row = 0; row < 12; row++) {
                    if (row == 0) {
                        html.append("<tr class='small'><td class='outline time' rowspan='12'>" + Time.timeString(early*60 + time*60) + "</td>");
                    }
                    else {
                        html.append("<tr class='small'>");
                    }
                    for (int col = 0; col < 5; col++) {
                        String c = getHTMLSchedule(schedule, col, Time.timeString(early*60 + time*60 + row*5));
                        if (c != null) {
                            String[] s = c.split("--");
                            html.append("<td " + "class='outline fill center' rowspan='" + s[1] + "' style='" +
                                        "background-color:" + s[2] + " !important;'>");
                            html.append(s[0]);
                            html.append("</td>");
                        }
                        else if (!schedule.isBusy(col, (early*60 + time*60 + row*5))) {
                            html.append("<td></td>");
                        }
                    }
                    html.append("</tr>");
                }
            }
            html.append("</table>");
        }
    }
    
    /**
     * The corresponding class that has the given day and start time
     * 
     * @param day the day specified
     * @param startTime the time specified
     * @return the class if it exists
     */
    private String getHTMLSchedule(Schedule schedule, int day, String startTime) {
        StringBuilder html = new StringBuilder();
        for (VTCourse c : schedule) {
            for (int i = 0; i < c.getTimes().size(); i++) {
                Time t = c.getTimes().get(i);
                String[] days = c.getDays().get(i);
                if (t != null) {
                    for (String d : days) {
                        if (Schedule.DAYS.indexOf(d) == day) {
                            if (t.getStart().equals(startTime)) {
                                html.append(c.getSubject() + " " + c.getNum() + "<br>");
                                //html.append(c.getCRN() + "<br>");
                                html.append(t.getStart() + " - " + t.getEnd() + "<br>");
                                html.append(c.getLocations().get(i) + "<br>");
                                html.append(c.getProf());
                                int start = Time.timeNumber(t.getStart())/5;
                                int end = Time.timeNumber(t.getEnd())/5;
                                html.append("--" + (end-start));
                                html.append("--" + colors[schedule.indexOf(c)]);
                                return html.toString();
                            }
                            break;
                        }
                    }
                }
            }
        }
        return null;
    }
}
