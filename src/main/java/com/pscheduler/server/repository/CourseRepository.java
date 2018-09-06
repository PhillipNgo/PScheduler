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
        + " WHERE (:query IS NULL OR LOWER(CONCAT(course.subject, ' ', course.courseNumber, ' ', course.name)) LIKE CONCAT('%', :query, '%'))"
        + " AND (:crn IS NULL OR course.crn = :crn)"
        + " AND (:subject IS NULL OR course.subject = :subject)"
        + " AND (:courseNumber IS NULL OR course.courseNumber = :courseNumber)"
        + " AND (:type IS NULL OR course.type = :type)"
        + " AND (:instructor IS NULL OR course.instructor = :instructor)"
        + " AND (:capacity IS NULL OR course.capacity = :capacity)"
        + " AND (:credits IS NULL OR course.credits = :credits)"
        + " AND (:name IS NULL OR course.name = :name)"
        + " AND (:term IS NULL OR course.term = :term)"
    )
    @RestResource(exported = false)
    Page<Course> searchAll(
        @Param("query") String query,
        @Param("crn") Integer crn,
        @Param("subject") String subject,
        @Param("courseNumber") String courseNumber,
        @Param("type") String type,
        @Param("instructor") String instructor,
        @Param("capacity") Integer capacity,
        @Param("credits") Integer credits,
        @Param("name") String name,
        @Param("term") Integer term,
        Pageable page
    );
}
