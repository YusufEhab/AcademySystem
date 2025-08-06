package com.academy.graduation.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private String id;
    private String name;
    private String email;
    private List<String> enrolledCourses;
}
