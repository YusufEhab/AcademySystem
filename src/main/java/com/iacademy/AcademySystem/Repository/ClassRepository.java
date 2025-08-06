package com.iacademy.AcademySystem.Repository;

import com.iacademy.AcademySystem.Document.ClassRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassRepository extends MongoRepository<ClassRoom, String> {

}
