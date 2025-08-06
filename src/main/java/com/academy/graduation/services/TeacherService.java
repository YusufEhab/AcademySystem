package com.academy.graduation.services;

import com.academy.graduation.dtos.TeacherDto;
import com.academy.graduation.dtos.TeacherRequest;
import com.academy.graduation.dtos.TeacherUpdate;
import com.academy.graduation.entities.Teacher;
import com.academy.graduation.mappers.TeacherMapper;
import com.academy.graduation.repositories.TeacherRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class TeacherService {
    @Autowired
    private TeacherRepo teacherRepo;
    @Autowired
    private TeacherMapper teacherMapper;

    public Page<TeacherDto> getAll(String name, String email, Pageable pageable) {
        Page<Teacher> teachers;
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (name != null) {
            teachers = teacherRepo.findByNameContainingIgnoreCaseAndUserId(name, userId, pageable);
        } else {
            teachers = teacherRepo.findByUserId(userId, pageable);
        }
        return teachers.map(teacherMapper::toDto);
    }

    public ResponseEntity<TeacherDto> getOne(String id) {
        var teacher = teacherRepo.findById(id).orElse(null);
        if (teacher == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(teacherMapper.toDto(teacher));
    }

    public ResponseEntity<TeacherDto> create(@Valid TeacherRequest request, UriComponentsBuilder uriBuilder) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        var teacher = teacherMapper.toEntity(request);
        teacher.setUserId(userId);

        teacherRepo.save(teacher);

        var teacherDto = teacherMapper.toDto(teacher);
        var uri = uriBuilder.path("/teachers/{id}").buildAndExpand(teacher.getId()).toUri();

        return ResponseEntity.created(uri).body(teacherDto);
    }

    public ResponseEntity<TeacherDto> update(String id, @Valid TeacherUpdate update) {
        var teacher = teacherRepo.findById(id).orElse(null);
        if (teacher == null) return ResponseEntity.notFound().build();

        teacherMapper.updateTeacher(update, teacher);
        teacherRepo.save(teacher);

        return ResponseEntity.ok(teacherMapper.toDto(teacher));
    }

    public ResponseEntity<Void> delete(String id) {
        var teacher = teacherRepo.findById(id).orElse(null);
        if (teacher == null) return ResponseEntity.notFound().build();

        teacherRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
