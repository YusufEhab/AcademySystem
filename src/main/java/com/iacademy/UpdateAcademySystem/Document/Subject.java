package com.iacademy.AcademySystem.Document;
import com.iacademy.AcademySystem.Dto.SubjectDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "subjects")
public class Subject {

    @Id
    private String id;

    private String name;

    private String description;

    private String teacherId;



}
