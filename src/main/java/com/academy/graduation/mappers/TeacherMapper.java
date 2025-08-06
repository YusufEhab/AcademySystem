package com.academy.graduation.mappers;

import com.academy.graduation.dtos.TeacherRequest;
import com.academy.graduation.dtos.TeacherDto;
import com.academy.graduation.dtos.TeacherUpdate;
import com.academy.graduation.entities.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TeacherMapper {
    TeacherDto toDto(Teacher teacher);
    Teacher toEntity(TeacherRequest teacherRequest);
    void updateTeacher(TeacherUpdate update, @MappingTarget Teacher teacher);

}
