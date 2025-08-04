package com.iacademy.AcademySystem.Dto;

import com.iacademy.AcademySystem.Document.Subject;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectDto {

    private String id;

    @NotBlank(message = "Subject name is required")
    private String name;

    private String description;

    @NotBlank(message = "Teacher ID is required")
    private String teacherId;

    public SubjectDto(Subject subject) {
        this.id = subject.getId();
        this.name = subject.getName();
        this.description = subject.getDescription();
        this.teacherId = subject.getTeacherId();
    }
}
