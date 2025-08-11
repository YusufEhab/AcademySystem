package com.iacademy.AcademySystem.Dto;
import com.iacademy.AcademySystem.Document.ClassRoom;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassRoomDto {

    private String id;

    @NotBlank(message = "Class name is required")
    private String name;

    @NotBlank(message = "Date is required")
    private String date;

    @NotBlank(message = "Subject ID is required")
    private String subjectId;

    @NotBlank(message = "Teacher ID is required")
    private String teacherId;

    private List<String> studentIds;


}
