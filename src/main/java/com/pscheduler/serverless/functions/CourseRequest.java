package com.pscheduler.serverless.functions;

public class CourseRequest {
    int term;
    String query;

    public CourseRequest() {}

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
