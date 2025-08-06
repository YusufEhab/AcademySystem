package com.academy.graduation.repositories;

import com.academy.graduation.entities.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepo extends MongoRepository<Teacher, String> {
    Page<Teacher> findAll(Pageable pageable);
    Page<Teacher> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Teacher> findByEmail(String email, Pageable pageable);
    Page<Teacher> findByNameContainingIgnoreCaseAndEmail(String name, String email, Pageable pageable);
    Page<Teacher> findByUserId(String userId, Pageable pageable);
    Page<Teacher> findByNameContainingIgnoreCaseAndUserId(String name, String userId, Pageable pageable);

}
