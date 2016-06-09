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
		html.append("<a href=\"excelsheets/ExcelSchedules.xls\" download=\"ExcelSchedules.xls\">Download as Excel</a>");
		html.append("</div>");
		
		
		html.append("<div class=\"parent\">");
		html.append("<div class=\"child box2\">");
		//MIDDLE--------------------------------------------------------------------------------------
		
		try {
            ScheduleMaker gen = new ScheduleMaker();
            
            String startTime = request.getParameter("hour1")+ ":";
            if ((Integer.parseInt(request.getParameter("minute1"))-1)*5 == 0 || (Integer.parseInt(request.getParameter("minute1"))-1)*5 == 5) {
                startTime += "0";
            }
            startTime += ((Integer.parseInt(request.getParameter("minute1"))-1)*5);
            gen.setStart(startTime + request.getParameter("start"));
            
            String endTime = request.getParameter("hour2")+ ":";
            if ((Integer.parseInt(request.getParameter("minute2"))-1)*5 == 0 || (Integer.parseInt(request.getParameter("minute2"))-1)*5 == 5) {
                endTime += "0";
            }
            endTime += ((Integer.parseInt(request.getParameter("minute2"))-1)*5);
            gen.setEnd(endTime + request.getParameter("end"));
            
            String freeDay = request.getParameter("freeday");
            if (Timetable.isDay(freeDay)) {
                gen.setFreeDay(freeDay);
            }
            
            gen.setTerm(request.getParameter("term"));
            String[] classes = request.getParameter("schedule").split("xx");
            if (classes[0].length() != 0) {
                for (int i = 0; i < classes.length; i++) {
                    if (classes[i].length() == 5) {
                        gen.addCRN(classes[i]);
                    }
                    else {
                        gen.addClass(classes[i].substring(0, classes[i].length()-4), classes[i].substring(classes[i].length()-4, classes[i].length()));
                    }
                }
            }
            LinkedList<Schedule> schedules = gen.generateSchedules();
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
            }
            else if (schedules.size() > 999) {
                html.append("There were over 999 Schedules, please narrow your parameters");
            }
            else {
                ExcelSchedule excelGen = new ExcelSchedule(schedules.toArray());
                excelGen.getExcelFile("WebContent/excelsheets/ExcelSchedules.xls");
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
		html.append("<p class=\"view\">Phillip Ngo | <a href=\"https://github.com/PhillipNgo/Scheduler-Site\">View the Project on GitHub</a></p>");
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
        for (int i = 0; i < schedules.size(); i++) {
            if (i != 0) {
                html.append("<table id=\"" + i + "\" style=\"display: none;\">");
            }
            else {
                html.append("<table id=\"" + i + "\">");
            }
            html.append("<tr><td colspan=\"9\" class=\"center\">" + (i+1) + " of " + schedules.size() + "</td></tr>");
            LinkedList<Class> classList = schedules.get(i).getClasses();
            for (int j = 0; j < classList.size(); j++) {
                html.append("<tr class=\"left\">");
                Class c = classList.get(j);
                html.append("<td>" + c.getCRN() + spaces + "</td>");
                html.append("<td>" + c.getCollege() + " " + classList.get(j).getNum() + spaces + "</td>");
                html.append("<td>" + c.getName() + spaces + "</td>");
                html.append("<td>" + c.getClassType() + spaces + "</td>");
                html.append("<td>" + c.getCredits() + "C" + spaces + "</td>");
                html.append("<td>" + c.getProf() + spaces + "</td>");
                String[] days = c.getDays();
                String day = "";
                for (int k = 0; k < days.length; k++) {
                    day += days[k];
                }
                html.append("<td>" + day + spaces + "</td>");
                html.append("<td>" + c.getStartTime() + " - " + c.getEndTime() + spaces + "</td>");
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
            html.append("</table>");
        }      
	}
	
	private void appendTableSchedules(StringBuilder html, LinkedList<Schedule> schedules) {
        for (int i = 0; i < schedules.size(); i++) {
            Schedule currSchedule = schedules.get(i);
            if (i != 0) {
                html.append("<table class=\"border\" id=\"" + i + "\" style=\"display: none;\">");
            }
            else {
                html.append("<table class=\"border\" id=\"" + i + "\">");
            }
            
            html.append("<tr>");
            html.append("<td class=\"center\" style=\"width:103pt; height:35pt;\">" + (i+1) + " of " + schedules.size() + "</td>");
            html.append("<td class=\"outline center\" style=\"width:174pt; height:35pt;\">Monday</td>");
            html.append("<td class=\"outline center\" style=\"width:174pt; height:35pt;\">Tuesday</td>");
            html.append("<td class=\"outline center\" style=\"width:174pt; height:35pt;\">Wednesday</td>");
            html.append("<td class=\"outline center\" style=\"width:174pt; height:35pt;\">Thursday</td>");
            html.append("<td class=\"outline center\" style=\"width:174pt; height:35pt;\">Friday</td>");
            html.append("</tr>");
            
            Timetable timetable = currSchedule.getTimetable();
            int early = TimeSlot.parseTime(timetable.earliestTime())/60;
            int late = TimeSlot.parseTime(timetable.latestTime())/60;
            for (int time = 0; time < late - early + 1; time++) {
                for (int row = 0; row < 12; row++) {
                    if (row == 0) {
                        html.append("<tr class=\"small\"><td class=\"outline time\" rowspan=\"12\">" + TimeSlot.num2Time(early*60 + time*60) + "</td>");
                    }
                    else {
                        html.append("<tr class=\"small\">");
                    }
                    for (int col = 0; col < 5; col++) {
                        String c = currSchedule.getHTMLSchedule(col, TimeSlot.num2Time(early*60 + time*60 + row*5));
                        if (c != null) {
                            String[] s = c.split("--");
                            html.append("<td " + "class=\"outline fill center\" rowspan=\"" + s[1] + "\">");
                            html.append(s[0]);
                            html.append("</td>");
                        }
                        else if (!timetable.isBusy(col, (early*60 + time*60 + row*5)/5)) {
                            html.append("<td></td>");
                        }
                    }
                    html.append("</tr>");
                }
            }
            html.append("</table>");
        }
	}
}
