package com.iacademy.AcademySystem.Service;

import com.iacademy.AcademySystem.Document.Subject;
import com.iacademy.AcademySystem.Dto.SubjectDto;
import com.iacademy.AcademySystem.Exception.NotFoundException;
import com.iacademy.AcademySystem.Repository.SubjectRepository;
import com.iacademy.AcademySystem.model.PageResult;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final ModelMapper modelMapper;
    private final MongoTemplate mongoTemplate;


    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return "anonymous";
        return auth.getName();
    }


    public String create(SubjectDto dto) {
        String username = getCurrentUsername();
        Subject subject = modelMapper.map(dto, Subject.class);
        subjectRepository.save(subject);
        System.out.println("Subject created by user: " + username);
        return subject.getId();
    }


    public SubjectDto getById(String id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subject not found with id: " + id));
        return modelMapper.map(subject, SubjectDto.class);
    }


    public void update(String id, SubjectDto dto) {
        String username = getCurrentUsername();
        Subject existing = subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subject not found with id: " + id));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setTeacherId(dto.getTeacherId());

        subjectRepository.save(existing);
        System.out.println("Subject updated by user: " + username);
    }

    // حذف موضوع
    public void delete(String id) {
        if (!subjectRepository.existsById(id)) {
            throw new NotFoundException("Subject not found with id: " + id);
        }
        subjectRepository.deleteById(id);
    }


    public List<SubjectDto> getAll() {
        return subjectRepository.findAll()
                .stream()
                .map(subject -> modelMapper.map(subject, SubjectDto.class))
                .collect(Collectors.toList());
    }


    public PageResult<SubjectDto> search(Map<String, String> params) {
        String name = params.get("name");
        int page = 0;
        int size = 10;

        try {
            if (params.get("page") != null && params.get("page").matches("\\d+")) {
                page = Integer.parseInt(params.get("page"));
            }
            if (params.get("size") != null && params.get("size").matches("\\d+")) {
                size = Integer.parseInt(params.get("size"));
            }
        } catch (NumberFormatException ignored) {
        }

        Criteria criteria = new Criteria();
        if (name != null && !name.trim().isEmpty()) {
            criteria = Criteria.where("name").regex(".*" + name.trim() + ".*", "i");
        }

        Query query = new Query(criteria).skip((long) page * size).limit(size);

        List<Subject> subjects = mongoTemplate.find(query, Subject.class);

        long total = mongoTemplate.count(new Query(criteria), Subject.class);

        List<SubjectDto> results = subjects.stream()
                .map(s -> modelMapper.map(s, SubjectDto.class))
                .collect(Collectors.toList());

        return new PageResult<>(results, total, page, size);
    }


    public Map<String, Long> countSubjectsPerTeacher() {
        GroupOperation groupByTeacher = Aggregation.group("teacherId").count().as("count");
        Aggregation aggregation = Aggregation.newAggregation(groupByTeacher);

        // تأكد من اسم collection الخاص بالموضوع في MongoDB (غالبًا "subject" أو "subjects")
        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "subject", Map.class);

        Map<String, Long> response = new HashMap<>();
        for (Map result : results.getMappedResults()) {
            response.put((String) result.get("_id"), ((Number) result.get("count")).longValue());
        }
        return response;
    }
}
