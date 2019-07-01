package com.pscheduler.server.repository;

import com.pscheduler.server.model.GPACourse;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.repository.CrudRepository;


@RepositoryRestResource(collectionResourceRel="gpa")
public interface GPACourseRepository extends CrudRepository<GPACourse, Integer> {

}
