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
        	     Scanner scan = new Scanner(new File("WebContent/SelectOptions/TermOptions.txt"));
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
          <select class="optionfont" id="subject" onchange="displayNums(false)">
			  <option value="AAEC">AAEC - Agricultural and Applied Economics</option>
			  <option value="ACIS">ACIS - Accounting and Information Systems</option>
			  <option value="AFST">AFST - Africana Studies</option>
			  <option value="AHRM">AHRM - Apparel, Housing, and Resource Management</option>
			  <option value="AINS">AINS - American Indian Studies</option>
			  <option value="ALCE">ALCE - Agricultural, Leadership, and Community Education</option>
			  <option value="ALS">ALS - Agriculture and Life Sciences</option>
			  <option value="AOE">AOE - Aerospace and Ocean Engineering</option>
			  <option value="APS">APS - Appalachian Studies</option>
			  <option value="APSC">APSC - Animal and Poultry Sciences</option>
			  <option value="ARBC">ARBC - Arabic</option>
			  <option value="ARCH">ARCH - Architecture</option>
			  <option value="ART">ART - Art and Art History</option>
			  <option value="AS">AS - Military Aerospace Studies</option>
			  <option value="ASPT">ASPT - Alliance for Social, Political, Ethical, and Cultural Thought</option>
			  <option value="AT">AT - Agricultural Technology</option>
			  <option value="BC">BC - Building Construction</option>
			  <option value="BCHM">BCHM - Biochemistry</option>
			  <option value="BIOL">BIOL - Biological Sciences</option>
			  <option value="BIT">BIT - Business Information Technology</option>
			  <option value="BMES">BMES - Biomedical Engineering and Sciences</option>
			  <option value="BMSP">BMSP - Biomedical Sciences and Pathobiology</option>
			  <option value="BMVS">BMVS - Biomedical and Veterinary Sciences</option>
			  <option value="BSE">BSE - Biological Systems Engineering</option>
			  <option value="BTDM">BTDM - Biomedical Technology Development and Management</option>
			  <option value="C21S">C21S - 21st Century Studies</option>
			  <option value="CEE">CEE - Civil and Environmental Engineering</option>
			  <option value="CHE">CHE - Chemical Engineering</option>
			  <option value="CHEM">CHEM - Chemistry</option>
			  <option value="CHN">CHN - Chinese</option>
			  <option value="CINE">CINE - Cinema</option>
			  <option value="CLA">CLA - Classical Studies</option>
			  <option value="CMDA">CMDA - Computational Modeling and Data Analytics</option>
			  <option value="CNST">CNST - Construction</option>
			  <option value="COMM">COMM - Communication</option>
			  <option value="COS">COS - College of Science</option>
			  <option value="CRIM">CRIM - Criminology</option>
			  <option value="CS">CS - Computer Science</option>
			  <option value="CSES">CSES - Crop and Soil Environmental Sciences</option>
			  <option value="DASC">DASC - Dairy Science</option>
			  <option value="ECE">ECE - Electrical and Computer Engineering</option>
			  <option value="ECON">ECON - Economics</option>
			  <option value="EDCI">EDCI - Education, Curriculum and Instruction</option>
			  <option value="EDCO">EDCO - Counselor Education</option>
			  <option value="EDCT">EDCT - Career and Technical Education</option>
			  <option value="EDEL">EDEL - Educational Leadership</option>
			  <option value="EDEP">EDEP - Educational Psychology</option>
			  <option value="EDHE">EDHE - Higher Education</option>
			  <option value="EDIT">EDIT - Instructional Design and Technology</option>
			  <option value="EDP">EDP - Environmental Design and Planning</option>
			  <option value="EDRE">EDRE - Education, Research and Evaluation</option>
			  <option value="EDTE">EDTE - Technology Education</option>
			  <option value="ENGE">ENGE - Engineering Education</option>
			  <option value="ENGL">ENGL - English</option>
			  <option value="ENGR">ENGR - Engineering</option>
			  <option value="ENSC">ENSC - Environmental Science</option>
			  <option value="ENT">ENT - Entomology</option>
			  <option value="ESM">ESM - Engineering Science and Mechanics</option>
			  <option value="FA">FA - Fine Arts</option>
			  <option value="FIN">FIN - Finance</option>
			  <option value="FIW">FIW - Fish and Wildlife Conservation</option>
			  <option value="FL">FL - Foreign Languages</option>
			  <option value="FOR">FOR - Forest Resources and Environmental Conservation</option>
			  <option value="FR">FR - French</option>
			  <option value="FST">FST - Food Science and Technology</option>
			  <option value="GBCB">GBCB - Genetics, Bioinformatics, Computational Biology</option>
			  <option value="GEOG">GEOG - Geography</option>
			  <option value="GEOS">GEOS - Geosciences</option>
			  <option value="GER">GER - German</option>
			  <option value="GIA">GIA - Government and International Affairs</option>
			  <option value="GR">GR - Greek</option>
			  <option value="GRAD">GRAD - Graduate School</option>
			  <option value="HD">HD - Human Development</option>
			  <option value="HEB">HEB - Hebrew</option>
			  <option value="HIST">HIST - History</option>
			  <option value="HNFE">HNFE - Human Nutrition, Foods and Exercise</option>
			  <option value="HORT">HORT - Horticulture</option>
			  <option value="HTM">HTM - Hospitality and Tourism Management</option>
			  <option value="HUM">HUM - Humanities</option>
			  <option value="IDS">IDS - Industrial Design</option>
			  <option value="IS">IS - International Studies</option>
			  <option value="ISC">ISC - Integrated Science</option>
			  <option value="ISE">ISE - Industrial and Systems Engineering</option>
			  <option value="ITAL">ITAL - Italian</option>
			  <option value="ITDS">ITDS - Interior Design</option>
			  <option value="JPN">JPN - Japanese</option>
			  <option value="JUD">JUD - Judaic Studies</option>
			  <option value="LAHS">LAHS - Liberal Arts and Human Sciences</option>
			  <option value="LAR">LAR - Landscape Architecture</option>
			  <option value="LAT">LAT - Latin</option>
			  <option value="LDRS">LDRS - Leadership Studies</option>
			  <option value="MACR">MACR - Macromolecular Science and Engineering</option>
			  <option value="MATH">MATH - Mathematics</option>
			  <option value="ME">ME - Mechanical Engineering</option>
			  <option value="MGT">MGT - Management</option>
			  <option value="MINE">MINE - Mining Engineering</option>
			  <option value="MKTG">MKTG - Marketing</option>
			  <option value="MN">MN - Military Navy</option>
			  <option value="MS">MS - Military Science (AROTC)</option>
			  <option value="MSE">MSE - Materials Science and Engineering</option>
			  <option value="MTRG">MTRG - Meteorology</option>
			  <option value="MUS">MUS - Music</option>
			  <option value="NANO">NANO - Nanoscience</option>
			  <option value="NEUR">NEUR - Neuroscience</option>
			  <option value="NR">NR - Natural Resources</option>
			  <option value="NSEG">NSEG - Nuclear Science and Engineering</option>
			  <option value="PAPA">PAPA - Public Administration/Public Affairs</option>
			  <option value="PHIL">PHIL - Philosophy</option>
			  <option value="PHS">PHS - Population Health Sciences</option>
			  <option value="PHYS">PHYS - Physics</option>
			  <option value="PORT">PORT - Portuguese</option>
			  <option value="PPWS">PPWS - Plant Pathology, Physiology and Weed Science</option>
			  <option value="PSCI">PSCI - Political Science</option>
			  <option value="PSVP">PSVP - Peace Studies</option>
			  <option value="PSYC">PSYC - Psychology</option>
			  <option value="REAL">REAL - Real Estate</option>
			  <option value="RLCL">RLCL - Religion and Culture</option>
			  <option value="RTM">RTM - Research in Translational Medicine</option>
			  <option value="RUS">RUS - Russian</option>
			  <option value="SBIO">SBIO - Sustainable Biomaterials</option>
			  <option value="SOC">SOC - Sociology</option>
			  <option value="SPAN">SPAN - Spanish</option>
			  <option value="SPIA">SPIA - School of Public and International Affairs</option>
			  <option value="STAT">STAT - Statistics</option>
			  <option value="STL">STL - Science, Technology, &amp; Law</option>
			  <option value="STS">STS - Science and Technology Studies</option>
			  <option value="SYSB">SYSB - Systems Biology</option>
			  <option value="TA">TA - Theatre Arts</option>
			  <option value="TBMH">TBMH - Translational Biology, Medicine and Health</option>
			  <option value="UAP">UAP - Urban Affairs and Planning</option>
			  <option value="UH">UH - University Honors</option>
			  <option value="UNIV">UNIV - University Course Series</option>
			  <option value="VM">VM - Veterinary Medicine</option>
			  <option value="WGS">WGS - Women's and Gender Studies</option>
          </select>
            
         
          	<%
          		scan = new Scanner(new File("WebContent/SelectOptions/NumberOptions.txt"));
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
    <p class="view">Phillip Ngo | <a href="https://github.com/PhillipNgo/Scheduler-Site">View the Project on GitHub</a></p>
  </footer>
</body>

</html>