package com.pscheduler.server.controller;

import com.pscheduler.server.model.CourseGPA;
import com.pscheduler.server.repository.CourseGPARepository;
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
public class CourseGPAController {

    @Autowired
    private CourseGPARepository courseRepo;

    @Autowired
    private PagedResourcesAssembler<CourseGPA> assembler;

    @RequestMapping(value = "/gpa/search")
    public @ResponseBody ResponseEntity<?> getSearch(
            @RequestParam(value="query", required = false) List<String> query,
            @RequestParam(value="term", required = false) Integer term,
            @RequestParam(value="subject", required = false) String subject,
            @RequestParam(value="number", required = false) String number,
            @RequestParam(value="instructor", required = false) String instructor,
            @RequestParam(value="sort", required = false) List<String> sorts,
            Pageable page
    ) {
        try {
            Sort sort = sorts == null ? null : new Sort(Sort.Direction.ASC, sorts);
            Pageable pageRequest = new PageRequest(page.getPageNumber(), page.getPageSize(), sort);
            Page<CourseGPA> pagedCourses;

            if (query == null) {
                pagedCourses = courseRepo.searchAll(null, term, subject, number, instructor, pageRequest);
            } else {
                List<CourseGPA> courses; courses = query.stream().flatMap(q -> {
                    String searchTerm = q.toLowerCase().replaceAll(" ", "");
                    return courseRepo.searchAll(searchTerm, term, subject, number, instructor, pageRequest)
                            .getContent()
                            .stream();
                }).collect(Collectors.toList());
                pagedCourses = new PageImpl<>(courses, pageRequest, courses.size());
            }

            PagedResources<Resource<CourseGPA>> resources = assembler.toResource(pagedCourses);
            resources.add(linkTo(methodOn(CourseGPAController.class).getSearch(
                    query, term, subject, number, instructor, sorts, page
            )).withSelfRel());
            return ResponseEntity.ok(resources);
        } catch (Exception ex) {
            System.err.println(ex);
            return ResponseEntity.badRequest().build();
        }
    }
}
