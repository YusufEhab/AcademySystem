package com.iacademy.AcademySystem.Service.Implementaions;

import com.iacademy.AcademySystem.Document.ClassRoom;
import com.iacademy.AcademySystem.Dto.ClassRoomDto;
import com.iacademy.AcademySystem.Exception.NotFoundException;
import com.iacademy.AcademySystem.Mapper.ClassMapper;
import com.iacademy.AcademySystem.Repository.ClassRepository;
import com.iacademy.AcademySystem.Service.CrudService;
import com.iacademy.AcademySystem.Dto.PageResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClassService implements CrudService<ClassRoomDto, String> {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    // للحصول على اسم المستخدم الحالي من سياق الأمان
    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            throw new NotFoundException("User is not authenticated");
        }
        return auth.getName();
    }


    private boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            throw new NotFoundException("User is not authenticated");
        }
        return auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(role));
    }

    @Override
    public String create(ClassRoomDto dto) {
        if (!hasRole("ROLE_ADMIN")) {
            throw new AccessDeniedException("Access denied. Only ADMIN can create classes.");
        }
        if (classRepository.findByName(dto.getName()).isPresent()) {
            throw new RuntimeException("Class name already exists: " + dto.getName());
        }
        ClassRoom entity = classMapper.toEntity(dto);
        entity.setId(null);
        ClassRoom saved = classRepository.save(entity);
        System.out.println("Class created by user: " + getCurrentUsername());
        return saved.getId();
    }

    @Override
    public ClassRoomDto getById(String id) {
        ClassRoom entity = classRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Class not found with id: " + id));
        return classMapper.toDto(entity);
    }

    @Override
    public void update(String id, ClassRoomDto dto) {
        if (!hasRole("ROLE_ADMIN")) {
            throw new AccessDeniedException("Access denied. Only ADMIN can update classes.");
        }
        ClassRoom existing = classRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Class not found with id: " + id));
        ClassRoom updated = classMapper.toEntity(dto);
        updated.setId(id);
        classRepository.save(updated);
        System.out.println("Class updated by user: " + getCurrentUsername());
    }

    @Override
    public void delete(String id) {
        if (!hasRole("ROLE_ADMIN")) {
            throw new AccessDeniedException("Access denied. Only ADMIN can delete classes.");
        }
        ClassRoom existing = classRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Class not found with id: " + id));
        classRepository.delete(existing);
        System.out.println("Class deleted by user: " + getCurrentUsername());
    }

    @Override
    public List<ClassRoomDto> getAll() {
        List<ClassRoom> all = classRepository.findAll();
        return all.stream()
                .map(classMapper::toDto)
                .collect(Collectors.toList());
    }

    public PageResult<ClassRoomDto> search(Map<String, String> params) {
        String name = params.get("name");
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 0;
        int size = params.containsKey("size") ? Integer.parseInt(params.get("size")) : 10;

        Pageable pageable = PageRequest.of(page, size);
        Query query = new Query();

        if (name != null && !name.isEmpty()) {
            query.addCriteria(Criteria.where("name").regex(name, "i"));
        }

        long count = mongoTemplate.count(query, ClassRoom.class);
        query.with(pageable);

        List<ClassRoom> results = mongoTemplate.find(query, ClassRoom.class);
        List<ClassRoomDto> dtos = results.stream()
                .map(classMapper::toDto)
                .collect(Collectors.toList());

        return new PageResult<>(
                dtos,
                count,
                page,
                size
        );
    }


    public Map<String, Long> countClassesByName() {
        GroupOperation groupByName = Aggregation.group("name").count().as("count");
        Aggregation aggregation = Aggregation.newAggregation(groupByName);
        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "classRoom", Map.class);

        Map<String, Long> counts = new HashMap<>();
        for (Map result : results) {
            String name = (String) result.get("_id");
            Long count = ((Number) result.get("count")).longValue();
            counts.put(name, count);
        }
        return counts;
    }
}
