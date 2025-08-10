package com.iacademy.AcademySystem.Document;
import com.iacademy.AcademySystem.Dto.ClassRoomDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "classrooms")
public class ClassRoom {

    @Id
    private String id;

    private String name;

    private String date;

    private String subjectId;

    private String teacherId;

    private List<String> studentIds;



}

