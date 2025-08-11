package com.iacademy.AcademySystem.Service.Implementaions;

import com.iacademy.AcademySystem.Document.Subject;
import com.iacademy.AcademySystem.Dto.PageResult;
import com.iacademy.AcademySystem.Dto.SubjectDto;
import com.iacademy.AcademySystem.Exception.NotFoundException;
import com.iacademy.AcademySystem.Mapper.SubjectMapper;
import com.iacademy.AcademySystem.Repository.SubjectRepository;
import com.iacademy.AcademySystem.Service.CrudService;

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
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class SubjectService implements CrudService<SubjectDto, String> {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;
    private final MongoTemplate mongoTemplate;

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
    public String create(SubjectDto dto) {
        if (!hasRole("ROLE_ADMIN")) {
            throw new AccessDeniedException("Access denied. Only ADMIN can create subjects.");
        }
        if (subjectRepository.findByName(dto.getName()).isPresent()) {
            throw new RuntimeException("Subject name already exists: " + dto.getName());
        }
        Subject entity = subjectMapper.toEntity(dto);
        entity.setId(null);
        Subject saved = subjectRepository.save(entity);
        System.out.println("Subject created by user: " + getCurrentUsername());
        return saved.getId();
    }

    @Override
    public SubjectDto getById(String id) {
        Subject entity = subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subject not found with id: " + id));
        return subjectMapper.toDto(entity);
    }

    @Override
    public void update(String id, SubjectDto dto) {
        if (!hasRole("ROLE_ADMIN")) {
            throw new AccessDeniedException("Access denied. Only ADMIN can update subjects.");
        }
        Subject existing = subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subject not found with id: " + id));
        Subject updated = subjectMapper.toEntity(dto);
        updated.setId(id);
        subjectRepository.save(updated);
        System.out.println("Subject updated by user: " + getCurrentUsername());
    }

    @Override
    public void delete(String id) {
        if (!hasRole("ROLE_ADMIN")) {
            throw new AccessDeniedException("Access denied. Only ADMIN can delete subjects.");
        }
        Subject existing = subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subject not found with id: " + id));
        subjectRepository.delete(existing);
        System.out.println("Subject deleted by user: " + getCurrentUsername());
    }

    @Override
    public List<SubjectDto> getAll() {
        List<Subject> all = subjectRepository.findAll();
        return all.stream()
                .map(subjectMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<SubjectDto> search(Map<String, String> params) {
        String name = params.get("name");
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 0;
        int size = params.containsKey("size") ? Integer.parseInt(params.get("size")) : 10;

        Pageable pageable = PageRequest.of(page, size);
        Query query = new Query();

        if (name != null && !name.isEmpty()) {
            query.addCriteria(Criteria.where("name").regex(name, "i"));
        }

        long count = mongoTemplate.count(query, Subject.class);
        query.with(pageable);

        List<Subject> results = mongoTemplate.find(query, Subject.class);
        List<SubjectDto> dtos = results.stream()
                .map(subjectMapper::toDto)
                .collect(Collectors.toList());

        return new PageResult<>(
                dtos,
                count,
                page,
                size
        );
    }

    public Map<String, Long> countSubjectsByName() {
        GroupOperation groupByName = Aggregation.group("name").count().as("count");
        Aggregation aggregation = Aggregation.newAggregation(groupByName);
        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "subject", Map.class);

        Map<String, Long> counts = new HashMap<>();
        for (Map result : results) {
            String name = (String) result.get("_id");
            Long count = ((Number) result.get("count")).longValue();
            counts.put(name, count);
        }
        return counts;
    }
}
