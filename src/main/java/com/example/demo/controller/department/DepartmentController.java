package com.example.demo.controller.department;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseObject;
import com.example.demo.dto.department.DepartmentRequest;
import com.example.demo.dto.department.DepartmentResponse;
import com.example.demo.service.DepartmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<ResponseObject> createDepartment(@Valid @RequestBody DepartmentRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return buildValidationErrorResponse(bindingResult);
        }

        departmentService.create(request);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Department created successfully")
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getDepartmentById(@PathVariable Long id) {
        DepartmentResponse response = departmentService.getById(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .data(response)
                .build());
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getAllDepartments() {
        List<DepartmentResponse> responses = departmentService.getAll();
        return ResponseEntity.ok(ResponseObject.builder()
                .data(responses)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateDepartment(@PathVariable Long id,
            @Valid @RequestBody DepartmentRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return buildValidationErrorResponse(bindingResult);
        }

        departmentService.update(id, request);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Department updated successfully")
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteDepartment(@PathVariable Long id) {
        departmentService.delete(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Department deleted successfully")
                .build());
    }

    private ResponseEntity<ResponseObject> buildValidationErrorResponse(BindingResult bindingResult) {
        List<String> errors = bindingResult.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity.badRequest()
                .body(ResponseObject.builder()
                        .message("Validation failed")
                        .data(errors)
                        .build());
    }
}

