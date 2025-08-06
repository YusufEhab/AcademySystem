package com.academy.graduation.services;

import com.academy.graduation.dtos.StudentDto;
import com.academy.graduation.dtos.StudentRequest;
import com.academy.graduation.dtos.StudentUpdate;
import com.academy.graduation.entities.Student;
import com.academy.graduation.mappers.StudentMapper;
import com.academy.graduation.repositories.StudentRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private StudentMapper studentMapper;
    public Page<StudentDto> getAll(String name, String email,  Pageable pageable) {
        Page<Student> students;
        if (name != null && email != null) {
            students = studentRepo.findByNameContainingIgnoreCaseAndEmail(name, email, pageable);
        } else if (name != null) {
            students = studentRepo.findByNameContainingIgnoreCase(name, pageable);
        } else if (email != null) {
            students = studentRepo.findByEmail(email, pageable);
        } else {
            students = studentRepo.findAll(pageable);
        }
        return students.map(studentMapper::toDto);
    }
    public ResponseEntity<StudentDto> getOne(String id) {
        var student = studentRepo.findById(id).orElse(null);
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        assert student != null;
        if (!student.getUserId().equals(currentUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(studentMapper.toDto(student));
    }
    public ResponseEntity<StudentDto> create(@Valid StudentRequest request, UriComponentsBuilder  uriBuilder) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        var student = studentMapper.toEntity(request);
        student.setUserId(userId);
        studentRepo.save(student);
        var studentDto = studentMapper.toDto(student);
        var uri = uriBuilder.path("/students/{id}").buildAndExpand(studentDto.getId()).toUri();
        return ResponseEntity.created(uri).body(studentDto);
    }
    public ResponseEntity<StudentDto> update(String id, @Valid StudentUpdate update) {
        var student = studentRepo.findById(id).orElse(null);
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        assert student != null;
        if (!student.getUserId().equals(currentUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        studentMapper.updateStudent(update,student);
        studentRepo.save(student);
        return ResponseEntity.ok(studentMapper.toDto(student));
    }

    public ResponseEntity<Void> delete(String id) {
        var student = studentRepo.findById(id).orElse(null);
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        assert student != null;
        if (!student.getUserId().equals(currentUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        studentRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
