package serv;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ds.LinkedList;
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
        String conflicts = ""; //holds html for search conflicts
        String courseSearch = ""; //holds html for the courses found on the timetable
        if (schedules != null) { //if there were no problems generating schedules
            restrictions = this.getRestrictions(); //get the restrictions
            conflicts = "This feature has been temporarily disabled."; //large schedule amounts slowed down the website
            try {
                //conflicts = this.getConflicts(); //get the conflicts
            }
            catch (Exception e) {}
            courseSearch = this.getCourseSearch(); //get the course search
        }
        
        
        // -- HEAD START -- // Imports, favicon, scripts
        html.append(
            "<!DOCTYPE html>" +
            "<html lang='en'>" +
            "<head>" +
                "<meta charset='utf-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1'>" +
                
                "<!-- Title and Favicon -->" +
                "<title>PScheduler | Schedule Creation</title>" +
                "<link rel='shortcut icon' href='favicon.ico' type='image/icon'>" +
                "<link rel='icon' href='favicon.ico' type='image/icon'>" +
                
                "<!-- JQuery -->" +
                "<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js'></script>" +
                
                "<!-- Bootstrap 3 -->" +
                "<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css'>" +
                "<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js'></script>" +
                
                "<!-- Bootstrap Select -->" +
                "<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.2/css/bootstrap-select.min.css'>" +
                "<script src='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.2/js/bootstrap-select.min.js'></script>" +
                
                "<!-- Style Sheet -->" +
                "<link rel='stylesheet' href='stylesheets/styles.css'>" +
                
                "<!-- Fonts -->" +
                "<link href='https://fonts.googleapis.com/css?family=Raleway|Roboto' rel='stylesheet'>" +
                
                "<!-- Scripts -->" +
                "<script type='text/javascript' src='javascripts/Schedules.js?5'></script>" +
                "<script type='text/javascript' src='javascripts/Classes.js?5'></script>" +
                "<script type='text/javascript' src='javascripts/Search.js?5'></script>" +
                
                "<!-- Google Analytics -->" +
                "<script>" +
                    "(function(i, s, o, g, r, a, m) {" +
                        "i['GoogleAnalyticsObject'] = r;" +
                        "i[r] = i[r] || function() {" +
                            "(i[r].q = i[r].q || []).push(arguments)" +
                        "}, i[r].l = 1 * new Date();" +
                        "a = s.createElement(o), m = s.getElementsByTagName(o)[0];" +
                        "a.async = 1;" +
                        "a.src = g;" +
                        "m.parentNode.insertBefore(a, m)" +
                    "})(window, document, 'script', 'https://www.google-analytics.com/analytics.js', 'ga');" +
                    "ga('create', 'UA-86032292-1', 'auto');" +
                    "ga('send', 'pageview');" +
                "</script>" +
            "</head>");
        // -- HEAD END -- //

        
        // -- BODY START -- //
        html.append("<body class='row'>");
        
            // -- HEADER START -- //
            html.append(
                "<div id='header' class='col-sm-1-5'>" +
                    "<nav>" +
                        "<ul class='menu-secondary'>" +
                            "<li id='gentab'><a href='#generator' onclick='changeSecondaryTab(this)'>Modify Search</a></li>" +
                            "<li id='schedtab' class='current'><a href='#schedules' onclick='changeSecondaryTab(this)'>Schedules</a></li>" +
                            "<li id='restab'><a href='#results' onclick='changeSecondaryTab(this)'>Timetable Results</a></li>" +
                        "</ul>" +
                        "<div></div>" +
                    "</nav>" +
                    "<nav class='bottom'>" +
                        "<ul class='menu-primary'>" +
                            "<li id='maingentab' class='current'><a href='#generator' onclick='changeTab(this)'>GENERATOR</a></li>" +
                            "<li id='contacttab'><a href='#contact' onclick='changeTab(this)'>CONTACT</a></li>" +
                        "</ul>" +
                        "<a href='http://www.pscheduler.com'><img class='logo' src='logo.jpg'></a>" +
                    "</nav>" +
                "</div>");
            // -- HEADER END -- //

            // -- SCHEDULES START -- //
            html.append(
                "<div id='schedules' class='col-sm-10-5'>" +
                    "<div class='main'>" +
                        "<div class='row'>" +
                            "<div class='col-sm-1 no-pad'>" +
                                "<input id='changet' style='text-align: center' type='text' class='form-control' value='1'>" +
                            "</div>" +
                            "<div class='col-sm-1'></div>" +
                            "<div class='col-sm-8 schedule-header'><h4 id='title'><b>");
                            
                            if (schedules == null || schedules.size() == 0) {
                                html.append("0 Schedules");
                            }
                            else if (schedules.size() >= 100) {
                                html.append("Schedule 1 of 100+");
                            }
                            else {
                                html.append("Schedule 1 of " + schedules.size());
                            }
                            
                            html.append("</b></h4></div>" +

                            "<div class='col-sm-2 media-group no-pad'>" +
                                "<div class='input-group-btn' style='text-align:right'>" +
                                    "<a id='download' href='download' onclick='dlHref()' type='button' class='btn btn-default'>" +
                                        "<span class='glyphicon glyphicon-download-alt'></span>" +
                                    "</a>" +
                                    "<button onclick='printerFriendly()' type='button' class='btn btn-default'>" +
                                        "<span class='glyphicon glyphicon-print'></span>" +
                                    "</button>" +
                                    "<button type='button' class='btn btn-default hidetb'>" +
                                        "<span id='hidetb-but' class='glyphicon glyphicon-triangle-top'></span>" +
                                    "</button>" +
                                "</div>" +
                            "</div>" +
                        "</div>");
                        
                        try {
                            if (schedules == null) {
                                html.append("Sorry, something went wrong when trying to generate your schedules.<br>");
                                html.append("Please head over to the contact tab and submit this link to our bug report form, thanks!");
                            }
                            else if (schedules.size() == 0) {
                                html.append("<div class='panel panel-warning'><div class='panel-heading'>No Schedules Matched Your Parameters. Look below for more information.</div>");
                                html.append("<div class='panel panel-body'><h3 class='o'><b>Your Restrictions</b></h3>");
                                html.append(restrictions);
                                html.append("<br>");
                                html.append("<h3 class='o'><b>Courses Found on Timetable</b></h3>");
                                html.append(courseSearch.toString() + "</div></div>");
                            }
                            else {
                                html.append(
                                "<div id='schedlist' class='carousel'>" +
                                    "<div class='carousel-inner'>");
                                for (Schedule s : schedules) {
                                    html.append("<div class='item");
                                    if (s.equals(schedules.get(0))) {
                                        html.append(" active");
                                    }
                                    html.append("'>");
                                    appendTextSchedule(s);
                                    html.append("<br>");
                                    appendTableSchedule(s);
                                    html.append("</div>");
                                }
                                html.append(
                                    "</div><a class='left carousel-control' href='#schedlist' onclick='changeSchedule(\"prev\")'>" +
                                      "<span style='left:1vw;color:darkslategray' class='glyphicon glyphicon-chevron-left'></span>" +
                                      "<span class='sr-only'>Previous</span>" +
                                    "</a>" +
                                    "<a class='right carousel-control' href='#schedlist' onclick='changeSchedule(\"next\")'>" +
                                      "<span style='right:1vw;color:darkslategray' class='glyphicon glyphicon-chevron-right'></span>" +
                                      "<span class='sr-only'>Next</span>" +
                                    "</a>" +
                                "</div>");
                            }
                        }
                        catch (Exception e) {
                            html.append("There was an error when displaying your schedules!<br>");
                        }    
                            
                       html.append("</div>" +
                    "</div>");
                // -- SCHEDULES END -- //
                
                // -- TIMETABLE RESULTS START -- //
                html.append(
                    "<div style='display:none' id='results' class='col-sm-10-5'>" +
                        "<div class='main'>" +
                            "<h3 class='o'><b>Your Restrictions</b></h3>");
                            html.append(restrictions.toString() + 
                            "<br>");
                            html.append("<h3 class='o'><b>Timetable Results</b></h3>" +
                            courseSearch.toString());
                            html.append(
                        "</div>" +
                    "</div>");
                // -- TIMETABLE RESULTS END -- //
                
            // -- GENERATOR START -- //
            html.append(
                "<div id='generator' class='col-sm-10-5' style='display:none'>" +
                    "<div class='main'>" +
                        "<form id='form' class='panel panel-default' action='generate'>" +
                            "<div class='panel-heading'>" +
                                "<h2 class='o title'>Restrictions</h2>" +
                            "</div>" +
                            "<div class='panel-body'>" +
                                "<div class='row'>" +
                                    "<div class='col-sm-2'>" +
                                        "<h4>Term</h4>" +
                                        "<select name='term' id='term' class='selectpicker form-control'>" +
                                            "<option value='201809'>Fall 2018</option>" +
                                            "<option value='201801'>Spring 2018</option>" +
                                            "<option value='201709'>Fall 2017</option>" +
                                            "<option value='201701'>Spring 2017</option>" +
                                        "</select>" +
                                    "</div>" +
                                "</div>" +
                                
                                "<div class='row'>" +
                                    "<div class='col-sm-3'>" +
                                        "<h4>Start Time</h4>" +
                                        "<div class='row'>" +
                                            "<div class='col-sm-4 no-pad'>" +
                                                "<select name='h1' class='selectpicker form-control'>" +
                                                    "<option value='01'>01</option>" +
                                                    "<option value='02'>02</option>" +
                                                    "<option value='03'>03</option>" +
                                                    "<option value='04'>04</option>" +
                                                    "<option value='05'>05</option>" +
                                                    "<option value='06'>06</option>" +
                                                    "<option value='07'>07</option>" +
                                                    "<option value='08' selected='selected'>08</option>" +
                                                    "<option value='09'>09</option>" +
                                                    "<option value='10'>10</option>" +
                                                    "<option value='11'>11</option>" +
                                                    "<option value='12'>12</option>" +
                                                "</select>" +
                                            "</div>" +
                                            "<div class='col-sm-4 no-pad'>" +
                                                "<select name='m1' class='selectpicker form-control'>" +
                                                    "<option value='00'>00</option>" +
                                                    "<option value='05'>05</option>" +
                                                    "<option value='10'>10</option>" +
                                                    "<option value='15'>15</option>" +
                                                    "<option value='20'>20</option>" +
                                                    "<option value='25'>25</option>" +
                                                    "<option value='30'>30</option>" +
                                                    "<option value='35'>35</option>" +
                                                    "<option value='40'>40</option>" +
                                                    "<option value='45'>45</option>" +
                                                    "<option value='50'>50</option>" +
                                                    "<option value='55'>55</option>" +
                                                "</select>" +
                                            "</div>" +
                                            "<div class='col-sm-4 no-pad'>" +
                                                "<select name='start' class='selectpicker form-control'>" +
                                                    "<option value='am'>AM</option>" +
                                                    "<option value='pm'>PM</option>" +
                                                "</select>" +
                                            "</div>" +
                                        "</div>" +
                                    "</div>" +
                                    "<div class='col-sm-3'>" +
                                        "<h4>End Time</h4>" +
                                        "<div class='row'>" +
                                            "<div class='col-sm-4 no-pad'>" +
                                                "<select name='h2' class='selectpicker form-control'>" +
                                                    "<option value='01'>01</option>" +
                                                    "<option value='02'>02</option>" +
                                                    "<option value='03'>03</option>" +
                                                    "<option value='04'>04</option>" +
                                                    "<option value='05'>05</option>" +
                                                    "<option value='06'>06</option>" +
                                                    "<option value='07'>07</option>" +
                                                    "<option value='08' selected='selected'>08</option>" +
                                                    "<option value='09'>09</option>" +
                                                    "<option value='10'>10</option>" +
                                                    "<option value='11'>11</option>" +
                                                    "<option value='12'>12</option>" +
                                                "</select>" +
                                            "</div>" +
                                            "<div class='col-sm-4 no-pad'>" +
                                                "<select name='m2' class='selectpicker form-control'>" +
                                                    "<option value='00'>00</option>" +
                                                    "<option value='05'>05</option>" +
                                                    "<option value='10'>10</option>" +
                                                    "<option value='15'>15</option>" +
                                                    "<option value='20'>20</option>" +
                                                    "<option value='25'>25</option>" +
                                                    "<option value='30'>30</option>" +
                                                    "<option value='35'>35</option>" +
                                                    "<option value='40'>40</option>" +
                                                    "<option value='45'>45</option>" +
                                                    "<option value='50'>50</option>" +
                                                    "<option value='55'>55</option>" +
                                                "</select>" +
                                            "</div>" +
                                            "<div class='col-sm-4 no-pad'>" +
                                                "<select name='end' class='selectpicker form-control'>" +
                                                    "<option value='am'>AM</option>" +
                                                    "<option value='pm' selected='selected'>PM</option>" +
                                                "</select>" +
                                            "</div>" +
                                        "</div>" +
                                    "</div>" +
                                "</div>" +
                                "<div class='row'>" +
                                    "<div class='col-sm-2'>" +
                                        "<h4>Gap Time</h4>" +
                                        "<select name='gap' class='selectpicker form-control'>" +
                                            "<option value='5'>5</option>" +
                                            "<option value='10'>10</option>" +
                                            "<option value='15' selected='selected'>15 mins</option>" +
                                            "<option value='20'>20</option>" +
                                            "<option value='25'>25</option>" +
                                            "<option value='30'>30</option>" +
                                            "<option value='35'>35</option>" +
                                            "<option value='40'>40</option>" +
                                            "<option value='45'>45</option>" +
                                            "<option value='50'>50</option>" +
                                            "<option value='55'>55</option>" +
                                            "<option value='60'>1:00 hr</option>" +
                                            "<option value='65'>1:05</option>" +
                                            "<option value='70'>1:10</option>" +
                                            "<option value='75'>1:15</option>" +
                                            "<option value='80'>1:20</option>" +
                                            "<option value='85'>1:25</option>" +
                                            "<option value='90'>1:30</option>" +
                                            "<option value='95'>1:35</option>" +
                                            "<option value='100'>1:40</option>" +
                                            "<option value='105'>1:45</option>" +
                                            "<option value='110'>1:50</option>" +
                                            "<option value='115'>1:55</option>" +
                                            "<option value='120'>2:00</option>" +
                                        "</select>" +
                                    "</div>" +
                                    "<div class='col-sm-2'>" +
                                        "<h4>Free Days</h4>" +
                                        "<select name='free' class='selectpicker form-control' multiple>" +
                                            "<option value='M'>Monday</option>" +
                                            "<option value='T'>Tuesday</option>" +
                                            "<option value='W'>Wednesday</option>" +
                                            "<option value='R'>Thursday</option>" +
                                            "<option value='F'>Friday</option>" +
                                        "</select>" +
                                    "</div>" +
                                "</div>" +
                                "<div>" +
                                    "<h4>Current Schedule</h4>" +
                                    "<table id='schedule' class='table'>" +
                                        "<thead class='thead-inverse'>" +
                                            "<tr>" +
                                                "<td>CRN</td>" +
                                                "<td>Course</td>" +
                                                "<td>Title</td>" +
                                                "<td>Class Type</td>" +
                                                "<td>Professor</td>" +
                                                "<td></td>" +
                                            "</tr>" +
                                        "</thead>" +
                                        "<tbody></tbody>" +
                                    "</table>" +
                                "</div>" +
                            "</div>" +
                            "<div class='panel-footer'>" +
                                "<button class='btn btn-default btn-lg' type='button'" +
                                    "onClick='sendData()'>Create Schedules</button>" +
                                    "<span class='pad-left'>The number of schedules generated is now capped at 100</span>" +
                                //"<span class='pad-left'><input name='color' type='checkbox' checked>Colored Output</span>" +
                            "</div>" +
                        "</form>" +
                        "<div>" +
                            "<h3 class='o title pad'>Course/CRN Search</h3>" +
                            "<p>Real time search has been disabled. Please press 'enter' after your search or press the search button.</p>" + 
                            "<div class='input-group'>" +
                                "<div class='input-group-btn'>" +
                                    "<select id='searchtype' class='selectpicker' data-width='auto'>" +
                                        "<option value='course'>Course</option>" +
                                        "<option value='crn'>CRN</option>" +
                                    "</select>" +
                                "</div>" +
                                "<div>" +
                                    "<input type='text' class='form-control live-search-box'" +
                                        "placeholder='Search a Course, CRN, or Professor to add (PHYS 2305, 85124, Boyer, etc.)'>" +
                                "</div>" +
                                "<div class='input-group-btn'>" +
                                    "<button class='btn btn-default' onclick='search()'>Search</button>" +
                                "</div>" +
                            "</div>" +
                            "<ul id='search' class='list-group live-search-list'></ul>" +
                        "</div>" +
                    "</div>" +
                "</div>");
            //-- GENERATOR END --//
            
            //-- CONTACT START --//
            html.append(
            "<div id='contact' class='col-sm-10-5 pad-left pad-right' style='display:none'>" +
            "<div class='page-header'><h1>Contact</h1></div>" +
            "<p>Thanks for using the site!</p>" +
            "<p>If you run into any problems or have any ideas I could implement, feel free to email me!</p>" +
            "<p>Email: <a href='mailto:ngophill@vt.edu?Subject=PScheduler'>ngophill@vt.edu</a><br>" +
            "Github: <a href='https://github.com/PhillipNgo/pScheduler'>https://github.com/PhillipNgo/pScheduler</a></p>" +
            "<div class='page-header'><h1>Upcoming Features</h1></div>" +
            "<ul>" +
                "<li>Manual Schedule Building</li>" +
                "<li>Improved Timetable</li>" +
                "<li>Generator" +
                    "<ul>" +
                        "<li>Choose multiple professors</li>" +
                        "<li>Add specific break times</li>" +
                        "<li>Ability to choose Lecture+Lab/Recitation rather than adding two seperate classes</li>" +
                    "</ul>" +
                "</li>" +
            "</ul>" +
            "<div class='page-header'><h1>Bug Reports</h1></div>" +
            "<iframe src='https://docs.google.com/forms/d/e/1FAIpQLSc65OlYTvNx6xnITbyxOoo-ocGoGZECAfP3yjNf3LrRNNGOSQ/viewform?embedded=true'" +
              "overflow='hidden' width='100%' height='100%' frameborder='0' marginheight='0' marginwidth='0'>Loading...</iframe>" +
            "</div>");
            
            //-- CONTACT END --//
        html.append("</body>");
        // -- BODY END -- //
         
        html.append("</html>");
        // -- END -- //
         
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
                if ("HG".contains(split[0].charAt(split[0].length()-1) + "")) {
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
    private void appendTextSchedule(Schedule schedule) {
        html.append("<table class='text table table-condensed'><tr style='font-weight:bold'><td class='text'>CRN</td> <td  class='text'>Course</td> <td  class='text'>Title</td>"
                + "<td class='text'>Type</td><td  class='text'>Credits (" + schedule.totalCredits() + ")</td><td  class='text'>Instructor</td>"
                + "<td class='text'>Days</td> <td  class='text'>Time</td> <td  class='text'>Location</td></tr>");
        int j = 0;
        for (VTCourse c : schedule) {
            html.append(textClass(c, true, j));
            j++;
        }
        html.append("</table>");  
    }
    
    /**
     * Appends the visualized tables to the html, the all but the first are hidden
     * @param schedules the schedues
     * @throws TimeException
     */
    private void appendTableSchedule(Schedule schedule) throws TimeException {
        html.append("<table class='outline' style='text-align:center;width:100%'>");
        html.append("<tr>");
        html.append("<td class='center' style='width:9.09%; height:5vh;'>" + "</td>");
        html.append("<td class='outline center' style='width:18.18%;'>Monday</td>");
        html.append("<td class='outline center' style='width:18.18%;'>Tuesday</td>");
        html.append("<td class='outline center' style='width:18.18%;'>Wednesday</td>");
        html.append("<td class='outline center' style='width:18.18%;'>Thursday</td>");
        html.append("<td class='outline center' style='width:18.18%;'>Friday</td>");
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
