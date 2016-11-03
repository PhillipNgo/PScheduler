package scheduler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ScheduleGen
 */
@WebServlet("/ScheduleGen")
public class ScheduleGen extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScheduleGen() {
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

        // -- HEAD START -- ///
        html.append("<!DOCTYPE html>");
        html.append("<html lang='en'>");
        html.append("<head>");
        html.append("<title>PScheduler</title>");
        html.append("<meta charset='utf-8'>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1'>");
        html.append("<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css'>");
        html.append("<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js'></script>");
        html.append("<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js'></script>");
        html.append("<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/css/bootstrap-select.min.css'>");
        html.append("<script src='https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/js/bootstrap-select.min.js'></script>");
        html.append("<link rel='stylesheet' href='stylesheets/styles.css'>");
        html.append("<script>");
        html.append("(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){");
        html.append("(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),");
        html.append("m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)");
        html.append("})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');");
        html.append("ga('create', 'UA-86032292-1', 'auto');");
        html.append("ga('send', 'pageview');");
        html.append("</script>");
        html.append("</head>");
        // -- HEAD END -- ///

        // -- BODY START -- ///
        html.append("<body style='background-color: #eceeef'>");
        html.append("<div style='background-color: DarkSlateGray; border-bottom: 1px solid darkorange;'>");
        html.append("   <div class='container-fluid'>");
        html.append("          <h1>");
        html.append("                  <span style='color: darkorange'>P</span><span style='color: white'>Scheduler</span>");
        html.append("                  <small style='color: darkorange'><i>VT Schedule Creation</i></small>");
        html.append("               </h1>");
        html.append("           </div>");
        html.append("       </div>");
        html.append("       <div class='container-fluid'>");
        html.append("         <div style='padding-top: 10px;' class='row'>");
        html.append("             <div style='padding-left: 12.5px; padding-right: 12.5px;' class='col-sm-12'>");
        html.append("                 <div class=' panel panel-default outline'>");
        html.append("                        <div style='background-color: white;' class='panel-heading center'>");
        html.append("                          <div class='row'>");
        html.append("                              <div class='col-sm-2'>");
        html.append("                                  <div class='input-group'>");
        html.append("                                     <div class='row'>");
        html.append("                                   <div style='padding:0 0 0 5px;' class='col-sm-4'>");
        html.append("                                          <span class='input-group-btn'>");
        html.append("                                              <button type='button' class='btn btn-default glyphicon glyphicon-chevron-left form-control'></button>");
        html.append("                                           </span>");
        html.append("                                        </div>");
        html.append("                                        <div style='padding:1px 0 0 0;' class='col-sm-4'>");
        html.append("                                            <input class='form-control' type='text' value='1'> ");
        html.append("                                        </div>");
        html.append("                                        <div style='padding:0 3px 0 2px;' class='col-sm-4'>");
        html.append("                                    <span class='input-group-btn'>");
        html.append("                                     <button type='button' class='btn btn-default glyphicon glyphicon-chevron-right form-control'></button>");
        html.append("                        </span>");
        html.append("              </div>");
        html.append(" </div>");
        html.append("                                </div>");
        html.append("</div>");
        html.append("<div class='col-sm-8'>");
        html.append("<h4><b>2 Schedules | 17 credit hours</b></h4>");
        html.append("</div>");
        html.append("<div style='text-align:right' class='col-sm-1'>");
        html.append("<button type='button' class='btn btn-default'>Modify Search</button>");
        html.append("</div>");
        html.append("<div style='text-ling:right' class='col-sm-1'>");
        html.append("<button type='button' class='btn btn-default'>Hide Table</button>");
        html.append("</div>");
        html.append("</div>");
        html.append("</div>");
        html.append("<div style='padding-top:0px' class='panel-body'>");


        html.append("</div>");
        html.append("</div>");
        html.append("</div>");
        html.append("</div>");
        html.append("</div>");
        html.append("<div class='footer'>");
        html.append("<div style='padding: 2vh 15vw 2vh 15vw;' class='container-fluid'>");
        html.append("<div style='color:white;' class='row'>");
        html.append("<div class='col-sm-4 text-right'>");
        html.append("ngophill@vt.edu");
        html.append("</div>");
        html.append("<div class='col-sm-4 text-center'>");
        html.append("<a style='color:darkorange;' href='https://banweb.banner.vt.edu/ssb/prod/HZSKVTSC.P_DispRequest'>Virginia Tech Class Time Table</a>");
        html.append("</div>");
        html.append("<div class='col-sm-4 text-left'>");
        html.append("<a style='color:white;' href='https://github.com/PhillipNgo/Scheduler-Website'>View the Project on GitHub</a>");
        html.append("</div>");
        html.append("</div>");
        html.append("</div>");
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");
        // -- BODY END -- //
        
        printer.print(html.toString());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
