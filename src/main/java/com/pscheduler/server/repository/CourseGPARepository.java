package com.pscheduler.server.repository;

import com.pscheduler.server.model.CourseGPA;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(collectionResourceRel="gpa", path="gpa")
public interface CourseGPARepository extends PagingAndSortingRepository<CourseGPA, Integer> {
    @Query(
        "SELECT course FROM CourseGPA course"
        + " WHERE (:query IS NULL OR LOWER(CONCAT(course.subject, course.courseNumber, ' ', course.name)) LIKE CONCAT('%', :query, '%'))"
        + " AND (:term IS NULL OR course.term = :term)"
        + " AND (:subject IS NULL OR LOWER(course.subject) = LOWER(:subject))"
        + " AND (:courseNumber IS NULL OR LOWER(course.courseNumber) = LOWER(:courseNumber))"
        + " AND (:instructor IS NULL OR LOWER(course.instructor) = LOWER(:instructor))"
    )
    @RestResource(exported = false)
    Page<CourseGPA> searchAll(
            @Param("query") String query,
            @Param("term") Integer term,
            @Param("subject") String subject,
            @Param("courseNumber") String courseNumber,
            @Param("instructor") String instructor,
            Pageable page
    );
}
