# PScheduler

This repo represents [pscheduler.com](http://pscheduler.com/), an open source schedule generation and building tool for Virginia Tech students.

## Description

Using the course data found on the [Virginia Tech Course Timetable](https://banweb.banner.vt.edu/ssb/prod/HZSKVTSC.P_DispRequest),
a user can input the classes and restrictions they want for a chosen semester and the the app will generate possible schedules for the set of classes.
The generated courses are visualized in both text and 5-day block schedule.

## Technology

### Front End

* React
  * React-Router
* Redux / React-Redux
  * Redux-Form
  * Redux-Thunk
* Webpack
* Bootstrap 3.3
  * Bootstrap Select

### Back End

* AWS (Production / serverless)
  * DynamoDB
  * Lambda (Java)
  * API Gateway
  * S3
  * Route 53
* Spring Boot (Development / server)
  * Hibernate
  * H2
* HtmlUnit (Web Scraping)

## Environment Setup

I've simplified the environment set up so you can help out! Development requires running a front and back end server. The back end is a REST API that the front end will make simple requests for courses.

### Requirements

* Java 8+
* Gradle 3.5.1+
* Node 10.6.0+
* npm 6.1.0+

### Front End Server

Install Packages

```
npm install
```

The front end runs on a webpack-dev-server, the default port is 3000.
To change the port, edit `var frontendServerPort = 3000` in `webpack.config.js` and `frontend.port=3000` in `src/main/resources/application.properties`

To run the server
```
npm run dev
```

The content is served at `http://localhost:<port_number>/` and edits will automatically be reloaded in the page

### Back End Server

The backend is a Spring Boot API (for development) which uses an H2 database.
The code managaing this server is found in the `com.pscheduler.server` package while the production serverless code is contained in the `com.pscheduler.serverless` package.

The default port for the api is 8080. To change the port, edit `var backendServerPort = 8080;` in `webpack.config.js` and `server.port=8080` in `src/main/resources/application.properties`

To start the server
```
gradlew bootRun                // Bash
.\gradlew.bat bootRun          // Windows CMD
```

The API loads `src/main/resources/data/201809.txt` (which holds the data for Fall 2018 courses). To specify a different semester or load multiple semesters, edit `src/main/java/com/pscheduler/server/core/DatabaseLoader.java`.
`com.pscheduler.util.parser.CourseFileCreator` can be used to create new term files.
