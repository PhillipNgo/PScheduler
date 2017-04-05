package serv;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import parser.VTParser;
import scheduler.VTCourse;
import time.Time;

/**
 * Servlet implementation class LiveSearch
 * Searches the class options
 */
@WebServlet("/LiveSearch")
public class LiveSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StringBuilder html; //holds the html response
	private final int MAX_OPTIONS = 5; //max number of options to search for
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LiveSearch() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * Searches the database for classes that match the search term and creates a list of MAX_OPTIONS options to display
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    //Set up
	    html = new StringBuilder();
	    response.setContentType("text/html");
	    //Get search information
	    String searchTerm = request.getParameter("search"); //the search term
	    String searchType = request.getParameter("type"); //the type of course to search
	    String termYear = request.getParameter("term"); //the school term to search
	    
	    //create the hide search button
	    html.append("<li id='hide' class='list-group-item'><button onclick='hide()' id='hideb' type='button' class='btn btn-default form-control'>Hide Search</button></li>");
	    Scanner scan; //scanner to read from database
	    try {
            scan = new Scanner(new File("webapps/ROOT/Database/" + termYear + ".txt"));
        } catch (Exception e) {
            scan = new Scanner(new File("WebContent/Database/" + termYear + ".txt"));
        }
	    
	    int count = 0; //counts how many options that have been found
	    String next = null;
	    try {
            next = scan.nextLine();
        } catch (Exception e) {}
	    
	    while (count < MAX_OPTIONS && next != null) {
	        //searches database for the term
	        if (next.toLowerCase().replaceAll("[\\s-]", "").contains(searchTerm)) {
	            VTCourse c;
	            try {
                    c = VTParser.makeClass(next);
                } catch (Exception e1) {
                    continue;
                }
	            //if a match was found create either a crn or course listing based on the search type
	            if (searchType.equals("crn")) { //create crn listing
	                html.append("<li class='list-group-item'><button onclick='addCRN($(this));hide();' name='" + c.getCRN() + 
	                        "' class='btn btn-default btn-sm' type='button'>Add</button> ");
	                html.append(c.getCRN() + " / " + c.getSubject() + " " + c.getNum() + " - " + c.getName() + " / " + classType(c.getClassType()));
	                html.append(" / " + c.getProf());
	                for (int i = 0; i < c.getTimes().size(); i++) {
	                    Time t = c.getTimes().get(i);
	                    String[] days = c.getDays().get(i);
	                    if (t != null) {
	                        html.append(" / ");
	                        for (String d : days) {
	                            html.append(d);
	                        }
	                        html.append(" " + t.getStart() + " - " + t.getEnd());
	                    } 
	                }
	                try {
                        next = scan.nextLine();
                    } catch (NoSuchElementException nsee){next = null;}
                      catch (Exception e) {}
	                html.append("</li>");
	            } else { //create search listing
	                html.append("<li class='list-group-item'><button onclick='addClass($(this));hide();' name='" + c.getSubject() + 
	                        c.getNum() + "' class='btn btn-default btn-sm' type='button'>Add</button> ");
	                html.append(c.getSubject() + " " + c.getNum() + " - " + c.getName());
	                
	                String types = "";
	                String profs = "";
	                String crns = "";
	                String subj = c.getSubject();
	                String num = c.getNum();
	                while (c.getSubject().equals(subj) && c.getNum().equals(num)) {
	                    String type = classType(c.getClassType());
	                    if (!types.contains(type)) {
	                        if (types.length() != 0) {
	                            types += ", ";
	                        }
	                        types += type;
	                    }
	                    if (!profs.contains(c.getProf())) {
	                        if (profs.length() != 0) {
	                            profs += ", ";
	                        }
	                        profs += c.getProf();
	                    }
	                    if (crns.length() != 0) {
	                        crns += ", ";
	                    }
	                    crns  += c.getCRN();
	                    try {
	                        next = scan.nextLine();
                            c = VTParser.makeClass(next);
                        } catch (NoSuchElementException nsee){
                            next = null;
                            break;
                        } catch (Exception e) {}
	                }
	                html.append(" / " + types + " / " + profs + " / " + crns);
	                html.append("</li>");
	            }
	            count++;
	        } else {
	            try {
                    next = scan.nextLine();
                } catch (NoSuchElementException nsee){next = null;}
                  catch (Exception e) {}
	        }
	    }
	    response.getWriter().write(html.toString());
	    scan.close();
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
