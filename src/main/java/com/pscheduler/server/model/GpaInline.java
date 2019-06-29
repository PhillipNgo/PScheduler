package com.pscheduler.server.model;

import java.util.List;

import org.springframework.data.rest.core.config.Projection;

@Projection(name="gpaInline", types = { GpaCourse.class })
public interface GpaInline {
    public String getSubject();
}
