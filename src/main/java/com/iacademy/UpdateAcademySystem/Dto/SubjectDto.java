package com.iacademy.AcademySystem.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.iacademy.AcademySystem.Document.Subject;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectDto {
@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    @NotBlank(message = "Subject name is required")
    private String name;

    private String description;

    @NotBlank(message = "Teacher ID is required")
    private String teacherId;


}
