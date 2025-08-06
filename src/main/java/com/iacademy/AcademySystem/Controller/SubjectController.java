package com.iacademy.AcademySystem.Controller;

import com.iacademy.AcademySystem.Dto.SubjectDto;
import com.iacademy.AcademySystem.Service.SubjectService;
import com.iacademy.AcademySystem.model.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String create(@RequestBody SubjectDto dto) {
        return subjectService.create(dto);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public SubjectDto getById(@PathVariable String id) {
        return subjectService.getById(id);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void update(@PathVariable String id, @RequestBody SubjectDto dto) {
        subjectService.update(id, dto);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable String id) {
        subjectService.delete(id);
    }

    // الادمن والمستخدم العادي يمكنهم الحصول على كل المواد
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<SubjectDto> getAll() {
        return subjectService.getAll();
    }


    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public PageResult<SubjectDto> search(@RequestParam Map<String, String> params) {
        return subjectService.search(params);
    }
}
