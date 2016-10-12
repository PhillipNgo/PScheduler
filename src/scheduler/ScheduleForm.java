package scheduler;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ScheduleForm
 */
@WebServlet("/ScheduleForm")
public class ScheduleForm extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScheduleForm() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter printer = response.getWriter();
        StringBuilder html = new StringBuilder();
        
        html.append("<!doctype html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<title>Scheduler</title>");
        html.append("<link rel=\"stylesheet\" href=\"stylesheets/styles.css\">");
        html.append("<link rel=\"stylesheet\" href=\"stylesheets/table.css\">");
        html.append("<script src=\"javascripts/Schedules.js\" type=\"text/javascript\"></script>");
        html.append("</head>");
        
        html.append("<body class=\"Site\">");
        html.append("<header>");
        html.append("<h1>Scheduler</h1>");
        html.append("<p class=\"view\">VT Schedule Creation</p>");
        html.append("</header>");
        
        html.append("<div class=\"buttongroup\">");
        html.append("<input type=\"button\" value=\"Modify Search\"/>");
        html.append("<input name=\"table\" onclick=\"switchView(this.name)\" id=\"view1\" type=\"button\" value=\"Text View\"/>");
        html.append("<input type=\"button\" value=\"Previous Schedule\"/ onclick=\"changeSchedule(-1)\">");
        html.append("<input type=\"button\" value=\"Next Schedule\"/ onclick=\"changeSchedule(1)\"><br>");
        //html.append("<a href=\"WebContent/excelsheets/schedules.xls\" download=\"schedules.xls\">Download as Excel</a>"); // not working
        html.append("</div>");
        
        
        html.append("<div class=\"parent\">");
        html.append("<div class=\"child box2\">");
        //MIDDLE--------------------------------------------------------------------------------------
        
        try {
            String startTime = request.getParameter("hour1")+ ":";
            if ((Integer.parseInt(request.getParameter("minute1"))-1)*5 == 0 || (Integer.parseInt(request.getParameter("minute1"))-1)*5 == 5) {
                startTime += "0";
            }
            startTime += ((Integer.parseInt(request.getParameter("minute1"))-1)*5) + request.getParameter("start");
            
            String endTime = request.getParameter("hour2")+ ":";
            if ((Integer.parseInt(request.getParameter("minute2"))-1)*5 == 0 || (Integer.parseInt(request.getParameter("minute2"))-1)*5 == 5) {
                endTime += "0";
            }
            endTime += ((Integer.parseInt(request.getParameter("minute2"))-1)*5) + request.getParameter("end");
            
            String freeDay = request.getParameter("freeday");
            
            String term = request.getParameter("term");
            
            String[] classes = request.getParameter("schedule").split("xx");
            //String[] subjects = new String[classes.length];
            //String[] numbers = new String[classes.length];
            //String[] types = new String[classes.length];
            //boolean[] onlineAllowed = new boolean[classes.length];
            LinkedList<String> subjects = new LinkedList<>();
            LinkedList<String> numbers = new LinkedList<>();
            LinkedList<String> types = new LinkedList<>();
            LinkedList<String> crns = new LinkedList<>();
            if (classes[0].length() != 0) {
                for (int i = 0; i < classes.length; i++) {
                    if (classes[i].length() == 5) {
                        crns.add(classes[i]);
                    }
                    else {
                        subjects.add(classes[i].substring(0, classes[i].length()-4));
                        numbers.add(classes[i].substring(classes[i].length()-4, classes[i].length()));
                        types.add("L");
                    }
                }
            }
            LinkedList<Schedule> schedules = ScheduleMaker.generateSchedule(term, subjects, numbers, types, startTime, endTime, freeDay, crns);
            
            if (schedules.size() == 0) {
                html.append("No Schedules Matched Your Parameters");
            }
            else if (schedules.size() < 1000) {
                html.append("<ul id=\"textschedules\"  style=\"display: none;\" name=\"0\">");
                appendTextSchedules(html, schedules);
                html.append("</ul>");
                
                html.append("<ul id=\"tableschedules\" name=\"0\">");
                appendTableSchedules(html, schedules);
                html.append("</ul>");
                
                ExcelSchedule.outputFile(schedules);
            }
            else if (schedules.size() > 999) {
                html.append("There were over 999 Schedules, please narrow your parameters");
            }
        }
        catch (Exception e) {
            html.append("Error in Making Schedules<br>");
            html.append(e.getMessage());
        }
        
        //MIDDLE END----------------------------------------------------------------------------------
        html.append("</div>");
        html.append("</div>");
        
        html.append("<div class=\"buttongroup\">");
        html.append("<input type=\"button\" value=\"Modify Search\"/>");
        html.append("<input id=\"view2\" onclick=\"switchView(this.name)\" type=\"button\" value=\"Text View\"/>");
        html.append("<input type=\"button\" value=\"Previous Schedule\"/ onclick=\"changeSchedule(-1)\">");
        html.append("<input type=\"button\" value=\"Next Schedule\"/ onclick=\"changeSchedule(1)\">");
        html.append("</div>");
        
        html.append("<footer>");
        html.append("<p class=\"view\">Phillip Ngo | <a href=\"https://github.com/PhillipNgo/Scheduler-Website\">View the Project on GitHub</a></p>");
        html.append("</footer>");
        html.append("</body>");
        html.append("</html>");
        
        printer.print(html.toString());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }
    
    
    // Private Helper Methods -------------------------------------------------
    
    private void appendTextSchedules(StringBuilder html, LinkedList<Schedule> schedules) {
        String spaces = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        int i = 0;
        for (Schedule schedule : schedules) {
            if (i != 0) {
                html.append("<table id=\"" + (i++) + "\" style=\"display: none;\">");
            }
            else {
                html.append("<table id=\"" + (i++) + "\">");
            }
            html.append("<tr><td colspan=\"9\" class=\"center\">" + i + " of " + schedules.size() + "</td></tr>");
            for (VTCourse c : schedule) {
                html.append("<tr class=\"left\">");
                html.append("<td>" + c.getCRN() + spaces + "</td>");
                html.append("<td>" + c.getSubject() + " " + c.getNum() + spaces + "</td>");
                html.append("<td>" + c.getName() + spaces + "</td>");
                html.append("<td>" + c.getClassType() + spaces + "</td>");
                html.append("<td>" + c.getCredits() + "C" + spaces + "</td>");
                html.append("<td>" + c.getProf() + spaces + "</td>");
                String[] days = c.getDays();
                String day = "";
                for (int k = 0; k < days.length; k++) {
                    day += days[k];
                }
                Time t = c.getTimeSlot();
                if (t != null) {
                    html.append("<td>" + day + spaces + "</td>");
                    html.append("<td>" + t.getStart() + " - " + t.getEnd() + spaces + "</td>");
                    html.append("<td>" + c.getLocation() + "</td>");
                    html.append("</tr>");
                    if (c.getAdditionalDays() != null && c.getAdditionalTime() != null && c.getAdditionalLocation() != null) {
                        html.append("<tr><td></td><td></td><td></td><td></td><td></td><td></td>");
                        days = c.getAdditionalDays();
                        String addedDays = "";
                        for (int k = 0; k < days.length; k++) {
                            addedDays += days[k];
                        }
                        html.append("<td>" + addedDays + spaces + "</td>");
                        html.append("<td>" + c.getAdditionalTime().getStart() + " - " + c.getAdditionalTime().getEnd() + spaces + "</td>");
                        html.append("<td>" + c.getAdditionalLocation() + "</td>");
                        html.append("</tr>");
                    }
                }
            }
            html.append("</table>");
        }      
    }
    
    private void appendTableSchedules(StringBuilder html, LinkedList<Schedule> schedules) throws TimeException {
        int i = 0;
        for (Schedule schedule : schedules) {
            if (i != 0) {
                html.append("<table class=\"border\" id=\"" + (i++) + "\" style=\"display: none;\">");
            }
            else {
                html.append("<table class=\"border\" id=\"" + (i++) + "\">");
            }
            
            html.append("<tr>");
            html.append("<td class=\"center\" style=\"width:103pt; height:35pt;\">" + i + " of " + schedules.size() + "</td>");
            html.append("<td class=\"outline center\" style=\"width:174pt; height:35pt;\">Monday</td>");
            html.append("<td class=\"outline center\" style=\"width:174pt; height:35pt;\">Tuesday</td>");
            html.append("<td class=\"outline center\" style=\"width:174pt; height:35pt;\">Wednesday</td>");
            html.append("<td class=\"outline center\" style=\"width:174pt; height:35pt;\">Thursday</td>");
            html.append("<td class=\"outline center\" style=\"width:174pt; height:35pt;\">Friday</td>");
            html.append("</tr>");
            
            int early = schedule.earliestTime()/60;
            int late = schedule.lastestTime()/60;
            for (int time = 0; time < late - early + 1; time++) {
                for (int row = 0; row < 12; row++) {
                    if (row == 0) {
                        html.append("<tr class=\"small\"><td class=\"outline time\" rowspan=\"12\">" + Time.timeString(early*60 + time*60) + "</td>");
                    }
                    else {
                        html.append("<tr class=\"small\">");
                    }
                    for (int col = 0; col < 5; col++) {
                        String c = getHTMLSchedule(schedule, col, Time.timeString(early*60 + time*60 + row*5));
                        if (time == 2) {
                            System.out.println();
                        }
                        if (c != null) {
                            String[] s = c.split("--");
                            html.append("<td " + "class=\"outline fill center\" rowspan=\"" + s[1] + "\">");
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
            String[] days = c.getDays();
            if (days != null) {
                for (String d : days) {
                    if (Schedule.DAYS.indexOf(d) == day) {
                        Time t = c.getTimeSlot();
                        if (t.getStart().equals(startTime)) {
                            html.append(c.getSubject() + " " + c.getNum() + "<br>");
                            html.append("CRN: " + c.getCRN() + "<br>");
                            html.append(t.getStart() + " - " + t.getEnd() + "<br>");
                            html.append(c.getLocation() + "<br>");
                            html.append("Prof: " + c.getProf());
                            int start = Time.timeNumber(t.getStart())/5;
                            int end = Time.timeNumber(t.getEnd())/5;
                            html.append("--" + (end-start));
                            return html.toString();
                        }
                        break;
                    }
                }
            }
            if (c.getAdditionalDays() != null) {
                days = c.getAdditionalDays();
                for (String d : days) {
                    if (Schedule.DAYS.indexOf(d) == day) {
                        Time t = c.getAdditionalTime();
                        if (c.getAdditionalTime().getStart().equals(startTime)) {
                            html.append(c.getSubject() + " " + c.getNum() + "<br>");
                            html.append("CRN: " + c.getCRN() + "<br>");
                            html.append(t.getStart() + " - " + t.getEnd() + "<br>");
                            html.append(c.getAdditionalLocation() + "<br>");
                            html.append("Prof: " + c.getProf());
                            int start = Time.timeNumber(t.getStart())/5;
                            int end = Time.timeNumber(t.getEnd())/5;
                            html.append("--" + (end-start));
                            return html.toString();
                        }
                        break;
                    }
                }
            }
        }
        return null;
    }
}