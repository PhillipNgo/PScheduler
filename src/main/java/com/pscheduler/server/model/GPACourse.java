package com.pscheduler.server.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
public class GPACourse {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min = 2, max = 6)
    private String subject;

    @Version
    private long version;

    public GPACourse() {
    }

    public GPACourse(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
