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
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-86032292-1', 'auto');
  ga('send', 'pageview');
</script>
<body class="Site">
  <header>
    <h1><a href="http://pscheduler.us-west-2.elasticbeanstalk.com">Scheduler</a></h1>
    <p class="view">VT Schedule Creation</p>
    
  </header>
  <div class="parent">
    <div class="child box1">

      <form name="form" id="form" action="ScheduleForm.do">
        <h3>Term:</h3><br>
        <select name="term" id="term" onchange="displayNums(true)">
        	 <%
        	 	 Scanner scan;
        	     try {
        	         scan = new Scanner(new File("webapps/ROOT/SelectOptions/TermOptions.txt"));
        	     }
        	     catch (Exception e) {
        	         scan = new Scanner(new File("WebContent/SelectOptions/TermOptions.txt"));
        	     }
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
            	try {
                	scan = new Scanner(new File("webapps/ROOT/SelectOptions/SubjectOptions.txt"));
      			}
      			catch (Exception e) {
      		 		scan = new Scanner(new File("WebContent/SelectOptions/SubjectOptions.txt"));
      			}
            	
			    while (scan.hasNextLine()) {
			        out.print(scan.nextLine());
			    }
			    scan.close();
          	%>
         
          	<%
          		try {
          			scan = new Scanner(new File("webapps/ROOT/SelectOptions/NumberOptions.txt"));
          		}
          		catch (Exception e) {
          			scan = new Scanner(new File("WebContent/SelectOptions/NumberOptions.txt"));
          		}
			    while (scan.hasNextLine()) {
			        out.print(scan.nextLine());
			    }
			    scan.close();
          	%>

	      <input type="button" name="addClassButton" value="Add" onclick="addClass('Z')"/>
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
    <p>ngophill@vt.edu | <a href="https://github.com/PhillipNgo/Scheduler-Website">View the Project on GitHub</a> | 
    <a href="https://goo.gl/forms/CIeZtR1XndZCFdUH2">Submit a Bug or Suggestion</a> | <a href="changelog.html">Recent Changes</a></p>
  </footer>
</body>
<script type="text/javascript">
	function codeAddress() {
		var qs = (function(a) {
		    if (a == "") return {};
		    var b = {};
		    for (var i = 0; i < a.length; ++i)
		    {
		        var p=a[i].split('=', 2);
		        if (p.length == 1)
		            b[p[0]] = "";
		        else
		            b[p[0]] = decodeURIComponent(p[1].replace(/\+/g, " "));
		    }
		    return b;
		})(window.location.search.substr(1).split('&'));
		
		if (qs["term"] != undefined) {
			var list = ["term", "hour1", "minute1", "start", "hour2", "minute2", "end", "freeday"];
			for (var i = 0; i < list.length; i++) {
				var option = document.getElementsByName(list[i])[0];
				option.value = qs[list[i]];
			}
			displayNums(false);
			displaySubj();
			var classList = qs["classes"].split("xx");
			for (var i = 0; i < classList.length; i++) {
				var str = classList[i];
				var index = 0;
				if (str.charAt(str.length-1) == 'H') {
                    index = 1;
                }
				if (str.length == 5 && !isNaN(parseInt(str, 10))) {
					selectCRN(str);
				}
				else {
					selectClass(str.substring(1, str.length-4-index), str.substring(str.length-4-index, str.length));
					document.getElementById("row" + i).value = str.substring(0, 1);
				}			
			}
		}
		else {
			displayNums(false);
		}
	}
	window.onload = codeAddress;
</script>
</html>
