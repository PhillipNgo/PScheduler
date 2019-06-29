package com.pscheduler.server.repository;

import com.pscheduler.server.model.GpaCourse;
import com.pscheduler.server.model.GpaInline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = GpaInline.class)
public interface GPACourseRepository extends CrudRepository<GpaCourse, Integer> {

    /*@RestResource(exported = false)
    Page<GpaCourse> searchAll(
        @Param("subject") String subject,
        Pageable page
    );*/

}