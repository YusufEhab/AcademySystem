package com.iacademy.AcademySystem.Repository;

import com.iacademy.AcademySystem.Document.ClassRoom;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassRepository extends MongoRepository<ClassRoom, String> {

    Optional<ClassRoom> findByName(@NotBlank(message = "Class name is required") String name);
}
