package com.pscheduler.serverless.functions;

public class UrlRequest {

    String data;
    String prefix;

    public UrlRequest() {}

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
