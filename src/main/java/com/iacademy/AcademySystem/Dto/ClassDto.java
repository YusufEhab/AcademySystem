package com.iacademy.AcademySystem.Dto;
import com.iacademy.AcademySystem.Document.ClassRoom;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassDto {

    private String id;

    @NotBlank(message = "Class name is required")
    private String name;

    @NotBlank(message = "Term is required")
    private String term;

    @NotBlank(message = "Subject ID is required")
    private String subjectId;

    @NotBlank(message = "Teacher ID is required")
    private String teacherId;

    private List<String> studentIds;


    public ClassDto(ClassRoom room) {
        this.id = room.getId();
        this.name = room.getName();
        this.term = room.getTerm();
        this.subjectId = room.getSubjectId();
        this.teacherId = room.getTeacherId();
        this.studentIds = room.getStudentIds();
    }
}
