package com.springboot.web.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.springboot.web.dto.StudentRequest;
import com.springboot.web.dto.StudentResponse;
import com.springboot.web.entity.Student;
import com.springboot.web.exception.StudentNotFoundException;
import com.springboot.web.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public StudentResponse create(StudentRequest request) {
        Student student = toEntity(request);
        return toResponse(repository.save(student));
    }

    public List<StudentResponse> getAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public StudentResponse getById(Long id) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found: " + id));
        return toResponse(student);
    }

    public StudentResponse update(Long id, StudentRequest request) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found: " + id));

        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setCourse(request.getCourse());

        return toResponse(repository.save(student));
    }

    public void delete(Long id) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found: " + id));
        repository.delete(student);
    }

    private Student toEntity(StudentRequest request) {
        Student student = new Student();
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setCourse(request.getCourse());
        return student;
    }

    private StudentResponse toResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getEmail(),
                student.getCourse()
        );
    }
}
