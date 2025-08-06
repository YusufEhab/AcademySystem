package com.iacademy.AcademySystem.Repository;

import com.iacademy.AcademySystem.Document.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends MongoRepository<Subject, String> {

}
