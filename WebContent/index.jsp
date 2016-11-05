<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>PScheduler - Schedule Creation</title>
<link rel='shortcut icon' href='favicon.ico' type='image/icon'>
<link rel='icon' href='favicon.ico' type='image/icon'>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/css/bootstrap-select.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/js/bootstrap-select.min.js"></script>
<link rel="stylesheet" href="stylesheets/styles.css">
<script type="text/javascript" src="javascripts/search.js"></script>
<script type="text/javascript" src="javascripts/Classes.js"></script>

<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-86032292-1', 'auto');
  ga('send', 'pageview');
</script>

<%@ page import="java.util.Scanner"%>
<%@ page import="scheduler.HtmlSet"%>
<%@ page import="java.io.File"%>

</head>
<body style="background-color: #eceeef">
	<div class="header">
		<div style='padding-bottom:10px' class="container-fluid">
			<h1 style="font-size:350%">
				<a href='http://www.pscheduler.com' style='text-decoration:none'><span style="color: darkorange">P</span><span style="color: white">Scheduler</span></a>
				<small style="color: darkorange"><i>Virginia Tech Schedule Creation</i></small>
			</h1>
		</div>
		<ul class="nav nav-tabs">
			<li role="presentation" class="active"><a href="#">Schedule Generator</a></li>
			<li role="presentation" class="tab"><a href="#">Course Search</a></li>
			<li role="presentation"><a href="#">About Me</a></li>
		</ul>
	</div>
	<div class="container-fluid">
		<div style="padding-left: 5px;padding-right: 25px" class="row">
			<div class="col-sm-3">
			<form id="form" class="panel panel-default outline" action="generate">
				<div style="background-color: white" class="panel-heading">
					<h2 style="color:darkorange">Restrictions</h2>
				</div>
				<div class="panel-body">
					<div style="padding-bottom: 10px">
						<h4>Term</h4>
						<select name="term" id="term" class="selectpicker">
							<%
        	 	 				HtmlSet writer = new HtmlSet(out);
								writer.termOptions();
        	 				%>
						</select>
					</div>
					<h4>Start Time</h4>
					<div style="padding-bottom: 10px" class="row">
						<div style="padding-right: 1px;" class="col-sm-4">
							<select name="h1" class="selectpicker form-control">
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
						</div>
						<div style="padding-left: 1px;" class="col-sm-4">
							<select name="m1" class="selectpicker form-control">
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
						</div>
						<div style="padding-left: 5px;" class="col-sm-4">
							<select name="start" class="selectpicker form-control">
								<option value="am">AM</option>
      							<option value="pm">PM</option>
							</select>
						</div>
					</div>
					<h4>End Time</h4>
					<div style="padding-bottom:10px" class="row">
						<div style="padding-right:1px;" class="col-sm-4">
							<select name="h2" class="selectpicker form-control">
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
						</div>
						<div style="padding-left:1px;" class="col-sm-4">
							<select name="m2" class="selectpicker form-control">
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
						</div>
						<div style="padding-left:5px;" class="col-sm-4">
							<select name="end" class="selectpicker form-control">
								<option value="am">AM</option>
      							<option value="pm" selected="selected">PM</option>
							</select>
						</div>
					</div>
					<div style="padding-bottom:10px">
						<h4>Free Days</h4>
						<select name="free" class="selectpicker" multiple>
							<option value="M">Monday</option>
      						<option value="T">Tuesday</option>
				      		<option value="W">Wednesday</option>
				      		<option value="R">Thursday</option>
				      		<option value="F">Friday</option>
						</select>
					</div>
				</div>
				<div style="background-color:white;padding-bottom:10px" class="panel-footer">
						<button style="color:darkorange;width:100%" class="btn btn-default btn-lg" type="submit" onClick="sendData()"><b>Create Schedules</b></button>
				</div>
			</form>
			*Specific CRNs Ignore Time and Day Restrictions<br>
			*Class Data Last Updated: 11/4/2016
			</div>
      		
			<div style="text-align: center;" class="col-sm-9">

				<div class="input-group">
					<div class="input-group-btn">
						<select id="searchtype" class="selectpicker" data-width="auto">
							<option value="course">Course</option>
							<option value="crn">CRN</option>
						</select>
					</div>
					<div>
						<input type="text" class="form-control live-search-box"
							placeholder="Search a Course, CRN, or Professor to add (PHYS 2305, 85124, Boyer, etc.)">
					</div>
				</div>
				<ul id="search" class="list-group live-search-list"
					style="position: fixed; text-align: left;">
					<li id="hide" style="display:none" class="list-group-item"><button id="hideb" type="button" class="btn btn-default form-control">Hide Search</button></li>
					<%
					    writer.searchOptions();
					%>
				</ul>

				<h2 style="color:darkorange">Current Schedule</h2>
				<table id="schedule" class="table border" style="background-color:white;">
					<thead class="thead-inverse">
						<tr style="font-weight: bold;">
							<td>CRN</td>
							<td>Course</td>
							<td>Title</td>
							<td>Class Type</td>
							<td>Professor</td>
							<td></td>
						</tr>
					</thead>
					<tbody>
						
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div class="footer">
		<div style="padding: 2vh 15vw 2vh 15vw;" class="container-fluid">
			<div style="color:white;" class="row">
				<div class="col-sm-4 text-right">
					<a href='mailto:ngophill@vt.edu' style='color:white'>ngophill@vt.edu</a>
				</div>
				<div class="col-sm-4 text-center">
					<a style="color:darkorange;" href="https://banweb.banner.vt.edu/ssb/prod/HZSKVTSC.P_DispRequest">Virginia Tech Class Time Table</a>
				</div>
				<div class="col-sm-4 text-left">
					<a style="color:white;" href="https://github.com/PhillipNgo/Scheduler-Website">View the Project on GitHub</a>
				</div>
			</div>
		</div>
  	</div>
</body>
</html>