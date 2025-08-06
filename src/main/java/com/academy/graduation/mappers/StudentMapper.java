package com.academy.graduation.mappers;

import com.academy.graduation.dtos.StudentDto;
import com.academy.graduation.dtos.StudentRequest;
import com.academy.graduation.dtos.StudentUpdate;
import com.academy.graduation.entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentDto toDto(Student student);
    Student toEntity(StudentRequest studentRequest);
    void updateStudent(StudentUpdate update, @MappingTarget Student student);
}
