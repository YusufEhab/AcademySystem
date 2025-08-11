package com.iacademy.AcademySystem.Repository;

import com.iacademy.AcademySystem.Document.Subject;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends MongoRepository<Subject, String> {

    Optional<Subject> findByName(@NotBlank(message = "Subject name is required") String name);
}
