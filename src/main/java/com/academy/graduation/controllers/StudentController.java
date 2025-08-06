package com.academy.graduation.controllers;

import com.academy.graduation.dtos.StudentDto;
import com.academy.graduation.dtos.StudentRequest;
import com.academy.graduation.dtos.StudentUpdate;
import com.academy.graduation.services.StudentService;
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
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @GetMapping
    public ResponseEntity<Page<StudentDto>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @PageableDefault(size = 10, sort = "name") Pageable pageable){
        Page<StudentDto> result = studentService.getAll(name, email, pageable);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getOne(@PathVariable String id){
        return studentService.getOne(id);
    }
    @PostMapping
    public ResponseEntity<StudentDto> create(@Valid @RequestBody StudentRequest request, UriComponentsBuilder uriBuilder){
        return studentService.create(request, uriBuilder);
    }
    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> update(
            @PathVariable String id,
            @Valid @RequestBody StudentUpdate update){
        return studentService.update(id,update);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        return studentService.delete(id);
    }
}
