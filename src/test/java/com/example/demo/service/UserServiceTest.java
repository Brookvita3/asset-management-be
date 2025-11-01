package com.example.demo.service;

import com.example.demo.dto.user.UserRequest;
import com.example.demo.dto.user.UserResponse;
import com.example.demo.entity.Department;
import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private UserService userService;

    private Department department;
    private User user;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        department = Department.builder()
                .id(1L)
                .name("IT")
                .build();

        user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .password("password")
                .role(Role.ADMIN)
                .active(true)
                .department(department)
                .build();

        userRequest = new UserRequest();
        userRequest.setName("Jane Doe");
        userRequest.setEmail("jane@example.com");
        userRequest.setPassword("secret");
        userRequest.setRole(Role.STAFF);
        userRequest.setDepartmentId(1L);
    }

    @Test
    void testGetAll_ShouldReturnListOfUserResponses() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponse> responses = userService.getAll();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getName()).isEqualTo("John Doe");

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetById_ShouldReturnUserResponse_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = userService.getById(1L);

        assertThat(response.getName()).isEqualTo("John Doe");
        assertThat(response.getEmail()).isEqualTo("john@example.com");

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    @Test
    void testDelete_ShouldCallRepositoryDeleteById() {
        userService.delete(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testCreate_ShouldSaveUser_WhenDepartmentExists() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        userService.create(userRequest);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(captor.capture());

        User savedUser = captor.getValue();
        assertThat(savedUser.getName()).isEqualTo("Jane Doe");
        assertThat(savedUser.getDepartment().getName()).isEqualTo("IT");
        assertThat(savedUser.getActive()).isTrue();
    }

    @Test
    void testCreate_ShouldThrowException_WhenDepartmentNotFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.create(userRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Department not found");

        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdate_ShouldUpdateUser_WhenExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        userService.update(1L, userRequest);

        assertThat(user.getName()).isEqualTo("Jane Doe");
        assertThat(user.getRole()).isEqualTo(Role.STAFF);
        assertThat(user.getDepartment()).isEqualTo(department);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdate_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(99L, userRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");

        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdate_ShouldThrowException_WhenDepartmentNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(1L, userRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Department not found");

        verify(userRepository, never()).save(any());
    }

    @Test
    void testGetByDepartment_ShouldReturnListOfUserResponses() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(userRepository.findByDepartmentId(1L)).thenReturn(Arrays.asList(user));

        List<UserResponse> responses = userService.getByDepartment(1L);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getName()).isEqualTo("John Doe");

        verify(departmentRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByDepartmentId(1L);
    }

    @Test
    void testGetByDepartment_ShouldThrowException_WhenDepartmentNotFound() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getByDepartment(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Department not found");
    }
}
