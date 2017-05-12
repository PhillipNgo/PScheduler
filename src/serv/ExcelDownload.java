package serv;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ds.LinkedList;
import scheduler.ExcelSchedule;
import scheduler.Schedule;
import scheduler.ScheduleMaker;

/**
 * Servlet implementation class ExcelDownload
 */
@WebServlet("/ExcelDownload")
public class ExcelDownload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ExcelDownload() {
        super();
    }

	/**
	 * Sends an excelsheet with the schedules into the output stream
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    //set up response
	    OutputStream out = response.getOutputStream();
	    response.setContentType("application/xls");
        response.setHeader("Content-disposition", "attachment; filename=schedules.xls");

	    try {
	        //Creates the excel sheet and writes it to the response
	        LinkedList<Schedule> schedules = getSchedules(request);
            ExcelSchedule excel = new ExcelSchedule(schedules);
            excel.getWorkbook().write(out);
        }
        catch (Exception e) {
            try { //if there was a problem, just output an empty excel schedule
                LinkedList<Schedule> schedules = new LinkedList<Schedule>();
                schedules.add(new Schedule());
                ExcelSchedule excel = new ExcelSchedule(schedules);
                excel.getWorkbook().write(out);
            }
            catch (Exception e1) {}
        }
	    out.flush();
	}
	
	/**
	 * Creates the schedules using the same schedule restrictions that were searched
	 * @param request the request holding schedule parameters
	 * @return the list of valid schedules
	 * @throws Exception
	 */
	private LinkedList<Schedule> getSchedules(HttpServletRequest request) throws Exception {
        String start = request.getParameter("h1") + ":" + request.getParameter("m1") + request.getParameter("start");
        String end = request.getParameter("h2") + ":" + request.getParameter("m2") + request.getParameter("end");

        String[] freeDays = request.getParameterValues("free");
        
        String term = request.getParameter("term");
        String[] classes = request.getParameter("schedule").split("~");
        int minGap = Integer.parseInt(request.getParameter("gap"));
        LinkedList<String> subjects = new LinkedList<>();
        LinkedList<String> numbers = new LinkedList<>();
        LinkedList<String> types = new LinkedList<>();
        LinkedList<String> crns = new LinkedList<>();
        LinkedList<String> profs = new LinkedList<>();

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

        ScheduleMaker generator = new ScheduleMaker(term, subjects, numbers, types, start, end, freeDays, minGap, crns, profs);
        return generator.getSchedules();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
