package scheduler;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LiveSearch
 * Searches the class options
 */
@WebServlet("/LiveSearch")
public class LiveSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StringBuilder html;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LiveSearch() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * Searches options.txt for classes that match the search term and creates a list of 10 options to display
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    html = new StringBuilder();
	    response.setContentType("text/html");
	    String searchTerm = request.getParameter("search");
	    String searchType = request.getParameter("type");
	    String termYear = request.getParameter("term");
	    html.append("<li id='hide' class='list-group-item'><button onclick='hide()' id='hideb' type='button' class='btn btn-default form-control'>Hide Search</button></li>");
	    Scanner scan;
	    try {
            scan = new Scanner(new File("webapps/ROOT/Database/" + termYear + "options.txt"));
        }
        catch (Exception e) {
            scan = new Scanner(new File("WebContent/Database/" + termYear + "options.txt"));
        }
	    int count = 0;
	    while (count < 5 && scan.hasNextLine()) {
	        String next = scan.nextLine();
	        if (next.toLowerCase().contains(searchTerm)) {
	            String[] val = next.split(" / ");
	            try {
	                Integer.parseInt(val[0]);
	                if (!searchType.equals("crn")) {
	                    continue;
	                }
	                html.append("<li class='list-group-item'><button onclick='addCRN($(this));hide();' name='" + val[0] + "' class='btn btn-default btn-sm' type='button'>Add</button> " + next + "</li>");
	            }
	            catch (NumberFormatException e) {
	                if (!searchType.equals("course")) {
                        continue;
                    }
	                String[] course = val[0].split(" - ");
	                html.append("<li class='list-group-item'><button onclick='addClass($(this));hide();' name='" + course[0].replace(" ", "") + "' class='btn btn-default btn-sm' type='button'>Add</button> " + next + "</li>");
	            }
	            count++;
	        }
	    }
	    response.getWriter().write(html.toString());
	    scan.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
