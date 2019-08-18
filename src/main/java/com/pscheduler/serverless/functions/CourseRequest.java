package com.pscheduler.serverless.functions;

import java.util.List;

public class CourseRequest {

    int term;
    List<String> query;

    public CourseRequest() {}

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public List<String> getQuery() {
        return query;
    }

    public void setQuery(List<String> query) {
        this.query = query;
    }

}
