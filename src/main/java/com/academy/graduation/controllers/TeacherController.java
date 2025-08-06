package com.academy.graduation.controllers;

import com.academy.graduation.dtos.StudentDto;
import com.academy.graduation.dtos.TeacherDto;
import com.academy.graduation.dtos.TeacherRequest;
import com.academy.graduation.dtos.TeacherUpdate;
import com.academy.graduation.services.TeacherService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RestController
@RequestMapping("/teachers")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    @GetMapping
    public ResponseEntity<Page<TeacherDto>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @PageableDefault (size = 10, sort = "name") Pageable pageable){
        Page<TeacherDto> result = teacherService.getAll(name, email, pageable);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> getOne(@PathVariable String id){
        return teacherService.getOne(id);
    }
    @PostMapping
    public ResponseEntity<TeacherDto> create(@Valid @RequestBody TeacherRequest request, UriComponentsBuilder uriBuilder){
        return teacherService.create(request, uriBuilder);
    }
    @PutMapping("/{id}")
    public ResponseEntity<TeacherDto> update(
            @PathVariable String id,
            @Valid @RequestBody TeacherUpdate update){
        return teacherService.update(id,update);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        return teacherService.delete(id);
    }
}
