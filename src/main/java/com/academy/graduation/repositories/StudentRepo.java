package com.academy.graduation.repositories;

import com.academy.graduation.entities.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepo extends MongoRepository<Student, String> {
    Page<Student> findAll(Pageable pageable);
    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Student> findByEmail(String email, Pageable pageable);
    Page<Student> findByNameContainingIgnoreCaseAndEmail(String name, String email, Pageable pageable);
    Optional<Student> findByUserId(String userId);
}
