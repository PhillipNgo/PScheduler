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
        + " AND (:subject IS NULL OR course.subject = :subject)"
        + " AND (:courseNumber IS NULL OR course.courseNumber = :courseNumber)"
        + " AND (:name IS NULL OR course.name = :name)"
        + " AND (:instructor IS NULL OR course.instructor = :instructor)"
        + " AND (:crn IS NULL OR course.crn = :crn)"
        + " AND (:credits IS NULL OR course.credits = :credits)"
        + " AND (:gpa IS NULL OR course.gpa = :gpa)"
        + " AND (:students IS NULL OR course.students = :students)"
        + " AND (:A IS NULL OR course.A = :A)"
        + " AND (:B IS NULL OR course.B = :B)"
        + " AND (:C IS NULL OR course.C = :C)"
        + " AND (:D IS NULL OR course.D = :D)"
        + " AND (:F IS NULL OR course.F = :F)"
        + " AND (:withdraws IS NULL OR course.withdraws = :withdraws)"
        + " AND (:term IS NULL OR course.term = :term)"
    )
    @RestResource(exported = false)
    Page<CourseGPA> searchAll(
            @Param("query") String query,
            @Param("subject") String subject,
            @Param("courseNumber") String courseNumber,
            @Param("name") String name,
            @Param("instructor") String instructor,
            @Param("crn") Integer crn,
            @Param("credits") Integer credits,
            @Param("gpa") Double gpa,
            @Param("students") Integer students,
            @Param("A") Double A,
            @Param("B") Double B,
            @Param("C") Double C,
            @Param("D") Double D,
            @Param("F") Double F,
            @Param("withdraws") Integer withdraws,
            @Param("term") Integer term,
            Pageable page
    );
}
