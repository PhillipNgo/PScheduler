# PScheduler

This repo represents the PScheduler website that creates schedule permutations for Virginia Tech courses.
The website can be found [here](http://pscheduler.com/)

## Description

Using the course data found on the [Virginia Tech Course Timetable](https://banweb.banner.vt.edu/ssb/prod/HZSKVTSC.P_DispRequest), a user can input the classes they want to take for a chosen semester and the program will generate all possible schedules for the set of classes. The generated courses are visualized in both a text format and a five day hourly format.

Image of schedule restrictions page:
![Image of schedule restrictions page](http://imgur.com/oQIaa6K)

Image of generated schedules:
![Image of generated schedules](http://imgur.com/ovfwhSa)

## Features

* Ability to choose any recent semester
* Choose an underlying minimum start and maximum end times for every class
* Add day restrictions for when no class can be scheduled
* Ability to specify CRNs or Class Types and Professors
* Search data that shows all possible crns for the courses the user chooses
* Downloadable Excel format so the user can edit the schedules

## Upcoming Features

* Minimum gap time between classes
* The ability to choose a specific time and day NOT to have class
* A new and improved Timetable for general course searching
* Hand making schedules by adding courses from the new timetable

## Tech

The site is made to run on an Apache Tomcat Server using Java for the backend and HTML5, CSS3, and JavaScript for the front end.
It utilizes [HtmlUnit](http://htmlunit.sourceforge.net/) in order to retrieve class data and [Apache POI](https://poi.apache.org/) to generate the downloadable excel formatted schedule list.
