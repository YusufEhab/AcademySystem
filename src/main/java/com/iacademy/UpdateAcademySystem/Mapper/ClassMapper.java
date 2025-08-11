package com.iacademy.AcademySystem.Mapper;

import com.iacademy.AcademySystem.Document.ClassRoom;
import com.iacademy.AcademySystem.Dto.ClassRoomDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ClassMapper {

    private final ModelMapper modelMapper;

    public ClassMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ClassRoomDto toDto(ClassRoom classRoom) {
        return modelMapper.map(classRoom, ClassRoomDto.class);
    }

    public ClassRoom toEntity(ClassRoomDto classDto) {
        return modelMapper.map(classDto, ClassRoom.class);
    }
}
