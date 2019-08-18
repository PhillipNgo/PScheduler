package com.pscheduler.server.repository;

import com.pscheduler.server.model.Course;
import com.pscheduler.server.model.CourseInlineMeeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(excerptProjection = CourseInlineMeeting.class)
public interface CourseRepository extends PagingAndSortingRepository<Course, Integer> {
    @Query(
        "SELECT course FROM Course course"
        + " WHERE (:query IS NULL OR LOWER(CONCAT(course.subject, course.courseNumber, ' ', course.name)) LIKE CONCAT('%', :query, '%'))"
        + " AND (:term IS NULL OR course.term = :term)"
        + " AND (:crn IS NULL OR LOWER(course.crn) = LOWER(:crn))"
        + " AND (:subject IS NULL OR LOWER(course.subject) = LOWER(:subject))"
        + " AND (:courseNumber IS NULL OR LOWER(course.courseNumber) = LOWER(:courseNumber))"
        + " AND (:type IS NULL OR LOWER(course.type) = LOWER(:type))"
        + " AND (:instructor IS NULL OR LOWER(course.instructor) = LOWER(:instructor))"
    )
    @RestResource(exported = false)
    Page<Course> searchAll(
        @Param("query") String query,
        @Param("term") Integer term,
        @Param("crn") Integer crn,
        @Param("subject") String subject,
        @Param("courseNumber") String courseNumber,
        @Param("type") String type,
        @Param("instructor") String instructor,
        Pageable page
    );
}
