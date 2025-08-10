package com.iacademy.AcademySystem.Mapper;

import com.iacademy.AcademySystem.Document.Subject;
import com.iacademy.AcademySystem.Dto.SubjectDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SubjectMapper {

    private final ModelMapper modelMapper;

    public SubjectMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public SubjectDto toDto(Subject subject) {
        return modelMapper.map(subject, SubjectDto.class);
    }

    public Subject toEntity(SubjectDto subjectDto) {
        return modelMapper.map(subjectDto, Subject.class);
    }
}
