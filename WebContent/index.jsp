<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
  <title>Scheduler</title>
  <link rel="stylesheet" href="stylesheets/styles.css">
  <script src="javascripts/Classes.js" type="text/javascript"></script>
  <%@ page import="java.util.Scanner"%>
  <%@ page import="java.io.File"%>
</head>

<body class="Site">
  <header>
    <h1>Scheduler</h1>
    <p class="view">VT Schedule Creation</p>
  </header>
  <div class="parent">
    <div class="child box1">

      <form name="form" id="form" action="ScheduleForm.do">
        <h3>Term:</h3><br>
        <select name="term" id="term" onchange="displayNums(true)">
        	 <%
        	     Scanner scan = new Scanner(new File("webapps/ROOT/SelectOptions/TermOptions.txt"));
        	     while (scan.hasNextLine()) {
        	         out.print(scan.nextLine());
        	     }
        	     scan.close();
        	 %>
      	</select><br><br>
        <h3>Earliest Start:<br>
      	<select name="hour1">
       		<option value="1">01</option>
      		<option value="2">02</option>
      		<option value="3">03</option>
      		<option value="4">04</option>
      		<option value="5">05</option>
      		<option value="6">06</option>
      		<option value="7">07</option>
      		<option value="8" selected="selected">08</option>
      		<option value="9">09</option>
      		<option value="10">10</option>
      		<option value="11">11</option>
      		<option value="12">12</option>
      	</select>
      	:
      	<select name="minute1">
      		<option value="1">00</option>
      		<option value="2">05</option>
      		<option value="3">10</option>
      		<option value="4">15</option>
      		<option value="5">20</option>
      		<option value="6">25</option>
      		<option value="7">30</option>
      		<option value="8">35</option>
      		<option value="9">40</option>
      		<option value="10">45</option>
      		<option value="11">50</option>
      		<option value="12">55</option>
      	</select>
      	<select name="start">
      		<option value="am">AM</option>
      		<option value="pm">PM</option>
      	</select></h3> &nbsp;to&nbsp;
        <h3>Latest End:<br>
      	<select name="hour2">
      		<option value="1">01</option>
      		<option value="2">02</option>
      		<option value="3">03</option>
      		<option value="4">04</option>
      		<option value="5">05</option>
      		<option value="6">06</option>
      		<option value="7">07</option>
      		<option value="8" selected="selected">08</option>
      		<option value="9">09</option>
      		<option value="10">10</option>
      		<option value="11">11</option>
      		<option value="12">12</option>
      	</select>
      	:
      	<select name="minute2">
      		<option value="1">00</option>
      		<option value="2">05</option>
      		<option value="3">10</option>
      		<option value="4">15</option>
      		<option value="5">20</option>
      		<option value="6">25</option>
      		<option value="7">30</option>
      		<option value="8">35</option>
      		<option value="9">40</option>
      		<option value="10">45</option>
      		<option value="11">50</option>
      		<option value="12">55</option>
      	</select>
      	<select name="end">
      		<option value="am">AM</option>
      		<option value="pm" selected="selected">PM</option>
      	</select></h3><br><br>

        <h3>Free Day:<br>
      	<select name="freeday">
      		<option value="none">None</option>
      		<option value="M">Monday</option>
      		<option value="T">Tuesday</option>
      		<option value="W">Wednesday</option>
      		<option value="E">Thursday</option>
      		<option value="F">Friday</option>
      	</select></h3><br><br>

        <h3>Subjects and Course Numbers:</h3><br>
        <div id="add" class="addclass">
          Add Class:<br> 
            <%
          		scan = new Scanner(new File("webapps/ROOT/SelectOptions/SubjectOptions.txt"));
			    while (scan.hasNextLine()) {
			        out.print(scan.nextLine());
			    }
			    scan.close();
          	%>
         
          	<%
          		scan = new Scanner(new File("webapps/ROOT/SelectOptions/NumberOptions.txt"));
			    while (scan.hasNextLine()) {
			        out.print(scan.nextLine());
			    }
			    scan.close();
          	%>

	      <input type="button" name="addClassButton" value="Add" onclick="addClass()"/>
		  <div style="padding-top:10px;">Add CRN:<br><input type="text" id="crn" class="optionfont short"/>
          <input type="button" name="addCRNButton" value="Add" onclick="addCRN()"/>
          </div><br>
        </div>
        
        Current Schedule:
        <table id="scheduledisplay">
        	
        </table>
        
        <input type="submit" class="createbutton" value="Create Schedules" onclick="sendSchedule()"/>
      </form>
    </div>
    
  </div>
  <footer>
    <p class="view">Phillip Ngo | <a href="https://github.com/PhillipNgo/Scheduler-Website">View the Project on GitHub</a></p>
  </footer>
</body>

</html>