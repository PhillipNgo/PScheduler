package com.pscheduler.server.repository;

import com.pscheduler.server.model.Meeting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface MeetingRepository extends CrudRepository<Meeting, Integer> {}