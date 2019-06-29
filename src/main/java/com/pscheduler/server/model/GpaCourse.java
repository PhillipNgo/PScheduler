package com.pscheduler.server.model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@Entity
public class GpaCourse {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min = 2, max = 6)
    private String subject;

    @Version
    private long version;

    public GpaCourse() {
        this.subject = "hi";
    }

    public GpaCourse(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}