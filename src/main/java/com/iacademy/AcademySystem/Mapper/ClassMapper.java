package com.iacademy.AcademySystem.Mapper;

import com.iacademy.AcademySystem.Document.ClassRoom;
import com.iacademy.AcademySystem.Dto.ClassDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ClassMapper {

    private final ModelMapper modelMapper;

    public ClassMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ClassDto toDto(ClassRoom classRoom) {
        return modelMapper.map(classRoom, ClassDto.class);
    }

    public ClassRoom toEntity(ClassDto classDto) {
        return modelMapper.map(classDto, ClassRoom.class);
    }
}
