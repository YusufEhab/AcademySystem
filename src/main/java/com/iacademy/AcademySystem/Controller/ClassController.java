package com.iacademy.AcademySystem.Controller;

import com.iacademy.AcademySystem.Dto.ClassDto;
import com.iacademy.AcademySystem.Service.ClassService;
import com.iacademy.AcademySystem.model.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String create(@RequestBody ClassDto dto) {
        return classService.create(dto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ClassDto getById(@PathVariable String id) {
        return classService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void update(@PathVariable String id, @RequestBody ClassDto dto) {
        classService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable String id) {
        classService.delete(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<ClassDto> getAll() {
        return classService.getAll();
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public PageResult<ClassDto> search(@RequestParam Map<String, String> params) {
        return classService.search(params);
    }
}
