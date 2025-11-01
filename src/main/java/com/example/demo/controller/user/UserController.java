package com.example.demo.controller.user;

import com.example.demo.dto.ResponseObject;
import com.example.demo.dto.user.UserRequest;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllUser() {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.builder()
                .message("Get all user successful")
                .data(userService.getAll())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.builder()
                .message("Get user by id successful")
                .data(userService.getById(id))
                .build());
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<ResponseObject> getUsersByDepartment(@PathVariable Long departmentId) {
        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.builder()
                .message("Get users by department successful")
                .data(userService.getByDepartment(departmentId))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        userService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.builder()
                .message("Update user successful")
                .data(null)
                .build());
    }

    @PostMapping("")
    public ResponseEntity<ResponseObject> createUser(@RequestBody UserRequest request) {
        userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseObject.builder()
                .message("Create user successful")
                .data(null)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.builder()
                .message("Delete user successful")
                .data(null)
                .build());
    }
}
