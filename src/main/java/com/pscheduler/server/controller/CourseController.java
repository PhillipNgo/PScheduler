package com.pscheduler.server.controller;

import com.pscheduler.server.model.Course;
import com.pscheduler.server.repository.CourseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RepositoryRestController
public class CourseController {

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private PagedResourcesAssembler<Course> assembler;

    @RequestMapping(value = "/courses/search")
    public @ResponseBody ResponseEntity<?> getSearch(
        @RequestParam(value="query", required = false) List<String> query,
        @RequestParam(value="term", required = false) Integer term,
        @RequestParam(value="crn", required = false) Integer crn,
        @RequestParam(value="subject", required = false) String subject,
        @RequestParam(value="number", required = false) String number,
        @RequestParam(value="type", required = false) String type,
        @RequestParam(value="instructor", required = false) String instructor,
        @RequestParam(value="capacity", required = false) Integer capacity,
        @RequestParam(value="credits", required = false) Integer credits,
        @RequestParam(value="name", required = false) String name,
        @RequestParam(value="sort", required = false) List<String> sorts,
        Pageable page
    ) {
        try {
            Sort sort = sorts == null ? null : new Sort(Sort.Direction.ASC, sorts);
            Pageable pageRequest = new PageRequest(page.getPageNumber(), page.getPageSize(), sort);
            List<Course> courses = query.stream().flatMap(q -> {
                String searchTerm = q.toLowerCase().replaceAll(" ", "");
                return courseRepo.searchAll(
                    searchTerm, crn, subject, number, type, instructor,
                    capacity, credits, name, term, pageRequest
                ).getContent().stream();
            }).collect(Collectors.toList());
            Page<Course> pagedCourses = new PageImpl<>(courses, pageRequest, courses.size());
            PagedResources<Resource<Course>> resources = assembler.toResource(pagedCourses);
            resources.add(linkTo(methodOn(CourseController.class).getSearch(
                query, term, crn, subject, number, type, instructor, capacity, credits, name, sorts, page
            )).withSelfRel());
            return ResponseEntity.ok(resources);
        } catch (Exception ex) {
            System.out.println(ex);
            return ResponseEntity.badRequest().build();
        }
    }
}
