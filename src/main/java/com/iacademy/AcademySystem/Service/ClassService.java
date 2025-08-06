package com.iacademy.AcademySystem.Service;

import com.iacademy.AcademySystem.Document.ClassRoom;
import com.iacademy.AcademySystem.Dto.ClassDto;
import com.iacademy.AcademySystem.Exception.NotFoundException;
import com.iacademy.AcademySystem.Mapper.ClassMapper;
import com.iacademy.AcademySystem.Repository.ClassRepository;
import com.iacademy.AcademySystem.model.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClassService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private MongoTemplate mongoTemplate;


    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return "anonymous";
        return auth.getName();
    }


    private boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        for (GrantedAuthority authority : auth.getAuthorities()) {
            if (authority.getAuthority().equals(role)) return true;
        }
        return false;
    }


    public String create(ClassDto dto) {
        if (!hasRole("ROLE_ADMIN")) {
            throw new AccessDeniedException("Access denied. Only ADMIN can create classes.");
        }
        String username = getCurrentUsername();
        ClassRoom classRoom = classMapper.toEntity(dto);
        System.out.println("Class created by user: " + username);
        return classRepository.save(classRoom).getId();
    }

    public ClassDto getById(String id) {
        // لا شرط صلاحية هنا، يمكن تضيف حسب الحاجة
        ClassRoom classRoom = classRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Class not found with id: " + id));
        return classMapper.toDto(classRoom);
    }


    public void update(String id, ClassDto dto) {
        if (!hasRole("ROLE_ADMIN")) {
            throw new AccessDeniedException("Access denied. Only ADMIN can update classes.");
        }
        String username = getCurrentUsername();
        ClassRoom existing = classRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Class not found with id: " + id));
        ClassRoom updated = classMapper.toEntity(dto);
        updated.setId(id);
        System.out.println("Class updated by user: " + username);
        classRepository.save(updated);
    }


    public void delete(String id) {
        if (!hasRole("ROLE_ADMIN")) {
            throw new AccessDeniedException("Access denied. Only ADMIN can delete classes.");
        }
        if (!classRepository.existsById(id)) {
            throw new NotFoundException("Class not found with id: " + id);
        }
        classRepository.deleteById(id);
    }


    public List<ClassDto> getAll() {
        return classRepository.findAll()
                .stream()
                .map(classMapper::toDto)
                .collect(Collectors.toList());
    }


    public PageResult<ClassDto> search(Map<String, String> params) {
        String name = params.get("name");

        String pageStr = params.getOrDefault("page", "0");
        int page = pageStr.matches("\\d+") ? Integer.parseInt(pageStr) : 0;

        String sizeStr = params.getOrDefault("size", "10");
        int size = sizeStr.matches("\\d+") ? Integer.parseInt(sizeStr) : 10;

        Query query = new Query();
        if (name != null && !name.isEmpty()) {
            query.addCriteria(Criteria.where("name").regex(name, "i"));
        }

        long total = mongoTemplate.count(query, ClassRoom.class);

        query.with(PageRequest.of(page, size));

        List<ClassDto> results = mongoTemplate.find(query, ClassRoom.class)
                .stream()
                .map(classMapper::toDto)
                .collect(Collectors.toList());

        return new PageResult<>(results, total, page, size);
    }


    public Map<String, Long> countClassesPerTeacher() {
        GroupOperation groupByTeacher = Aggregation.group("teacherId").count().as("count");
        Aggregation aggregation = Aggregation.newAggregation(groupByTeacher);

        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "classrooms", Map.class);

        Map<String, Long> response = new HashMap<>();
        for (Map result : results.getMappedResults()) {
            response.put((String) result.get("_id"), ((Number) result.get("count")).longValue());
        }
        return response;
    }
}
