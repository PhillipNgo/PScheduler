package com.pscheduler.server.repository;

import com.pscheduler.server.model.CourseGPA;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="gpa", path="gpa")
public interface CourseGPARepository extends CrudRepository<CourseGPA, Integer> {}
