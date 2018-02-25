package com.vezixtor.ontormi.controller;

import com.vezixtor.ontormi.model.dto.ProjectDTO;
import com.vezixtor.ontormi.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/projects")
public class ProjectApiController {

    private final ProjectService projectService;

    @Autowired
    public ProjectApiController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<?> postCreate(@Valid @RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.ok(projectService.postCreate(projectDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") Long id) {
        return ResponseEntity.ok(projectService.getOne(id));
    }

    @PutMapping
    public ResponseEntity<?> putUpdate(@Valid @RequestBody ProjectDTO projectDTO) {
        return ResponseEntity.ok(projectService.putUpdate(projectDTO));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Long id) {
        projectService.delete(id);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(name = "previous", required = false) Long previous,
                                    @RequestParam(name = "next", required = false) Long next,
                                    @RequestParam(name = "limit", required = false, defaultValue = "5") Integer limit) {
        return ResponseEntity.ok(projectService.getAll(previous, next, limit));
    }
}
