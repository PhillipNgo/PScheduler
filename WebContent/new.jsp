<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>PScheduler</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/css/bootstrap-select.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.11.2/js/bootstrap-select.min.js"></script>
<link rel="stylesheet" href="stylesheets/styles.css">
<script type="text/javascript" src="javascripts/search.js"></script>
<%@ page import="java.util.Scanner"%>
<%@ page import="java.io.File"%>

</head>
<body style="background-color: #eceeef">
	<div class="header">
		<div class="container-fluid">
			<h1>
				<span style="color: darkorange">P</span><span style="color: white">Scheduler</span>
				<small style="color: darkorange"><i>VT Schedule Creation</i></small>
			</h1>
		</div>
	</div>
	<div class="container-fluid">
		<div style="padding-left: 5px;padding-right: 25px" class="row">
			<div class="col-sm-3">
			<form class="panel panel-default outline">
				<div style="background-color: white" class="panel-heading">
					<h2 style="color:darkorange">Restrictions</h2>
				</div>
				<div class="panel-body">
					<div style="padding-bottom: 10px">
						<h4>Term</h4>
						<select name="term" id="term" class="selectpicker">
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
				      		<option value="E">Thursday</option>
				      		<option value="F">Friday</option>
						</select>
					</div>
				</div>
				<div style="background-color:white;padding-bottom:10px" class="panel-footer">
						<button style="color:darkorange;width:100%" class="btn btn-default btn-lg" type="button"><b>Create Schedules</b></button>
				</div>
			</form>
			*Added CRNs Ignore Time and Day Restrictions<br>
			*Class Data Last Updated: 10/21/2016
			</div>
      		
			<div style="text-align: center;" class="col-sm-9">
				<input type="text" class="form-control live-search-box" placeholder="Search A Course">
				<ul id="search" class="list-group live-search-list" style="position:fixed;text-align:left;">
					<%
					    try {
					        scan = new Scanner(new File(
					                "webapps/ROOT/SelectOptions/201701options.html"));
					    }
					    catch (Exception e) {
					        scan = new Scanner(
					                new File("WebContent/SelectOptions/201701options.html"));
					    }

					    while (scan.hasNextLine()) {
					        out.print(scan.nextLine());
					    }
					    try {
					        scan = new Scanner(new File(
					                "webapps/ROOT/SelectOptions/201609options.html"));
					    }
					    catch (Exception e) {
					        scan = new Scanner(
					                new File("WebContent/SelectOptions/201609options.html"));
					    }

					    while (scan.hasNextLine()) {
					        out.print(scan.nextLine());
					    }
					    scan.close();
					%>
				</ul>
				
				<h2 style="color:darkorange">Current Schedule</h2>
				<table class="table border" style="background-color:white;">
					<thead class="thead-inverse">
						<tr style="font-weight: bold;">
							<td>#</td>
							<td>CRN</td>
							<td>Course</td>
							<td>Title</td>
							<td>Class Type</td>
							<td>Professor</td>
							<td></td>
						</tr>
					</thead>
					<tbody>
						<tr style="text-align:center">
							<td style="vertical-align:middle" scope="row">1</td>
							<td>
								<select class="selectpicker" data-width="auto">
									<option>Any</option>
									<option>11111</option>
								</select>
							</td>
							<td style="vertical-align:middle">ECE 2014</td>
							<td style="vertical-align:middle">ECE Professionalism</td>
							<td>
								<select class="selectpicker" data-width="auto">
									<option>Lecture</option>
									<option>Independent Study</option>
								</select>
							</td>
							<td>
								<select class="selectpicker" data-width="auto">
									<option>Ball</option>
									<option>Super Long Name</option>
								</select>
							</td>
							<td><button class="btn btn-default" type="button">Remove</button></td>
						</tr>
						
						
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div class="footer">
		<div style="padding: 2vh 15vw 2vh 15vw;" class="container-fluid">
			<div style="color:white;" class="row">
				<div class="col-sm-4 text-right">
					ngophill@vt.edu
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