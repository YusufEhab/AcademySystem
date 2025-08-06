package com.iacademy.AcademySystem.Document;
import com.iacademy.AcademySystem.Dto.ClassDto;
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

    private String term;

    private String subjectId;

    private String teacherId;

    private List<String> studentIds;

    public ClassRoom(ClassDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.term = dto.getTerm();
        this.subjectId = dto.getSubjectId();
        this.teacherId = dto.getTeacherId();
        this.studentIds = dto.getStudentIds();
    }

}

