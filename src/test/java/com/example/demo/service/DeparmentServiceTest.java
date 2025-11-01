package com.example.demo.service;

import com.example.demo.dto.department.DepartmentRequest;
import com.example.demo.dto.department.DepartmentResponse;
import com.example.demo.entity.Department;
import com.example.demo.exception.DataNotFound;
import com.example.demo.repository.DepartmentRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    private DepartmentRequest request;

    @BeforeEach
    void setup() {
        request = new DepartmentRequest();
        request.setName("IT");
        request.setDescription("Tech Department");
        request.setManagerId(1L);
        request.setIsActive(true);
        request.setEmployeeCount(10);
    }

    @Test
    void testCreate_ShouldSaveDepartment() {
        departmentService.create(request);

        ArgumentCaptor<Department> captor = ArgumentCaptor.forClass(Department.class);
        verify(departmentRepository, times(1)).save(captor.capture());

        Department saved = captor.getValue();
        assertThat(saved.getName()).isEqualTo("IT");
        assertThat(saved.getDescription()).isEqualTo("Tech Department");
        assertThat(saved.getManagerId()).isEqualTo(1L);
        assertThat(saved.getIsActive()).isTrue();
        assertThat(saved.getEmployeeCount()).isEqualTo(10);
    }

    @Test
    void testUpdate_ShouldUpdateExistingDepartment() {
        Long id = 1L;
        Department existing = Department.builder()
                .id(id)
                .name("Old Name")
                .description("Old Desc")
                .managerId(2L)
                .isActive(false)
                .employeeCount(5)
                .build();

        when(departmentRepository.findById(id)).thenReturn(Optional.of(existing));

        request.setName("New Name");
        request.setDescription("New Desc");
        request.setManagerId(3L);
        request.setIsActive(true);
        request.setEmployeeCount(15);

        departmentService.update(id, request);

        assertThat(existing.getName()).isEqualTo("New Name");
        assertThat(existing.getDescription()).isEqualTo("New Desc");
        assertThat(existing.getManagerId()).isEqualTo(3L);
        assertThat(existing.getIsActive()).isTrue();
        assertThat(existing.getEmployeeCount()).isEqualTo(15);

        verify(departmentRepository, times(1)).save(existing);
    }

    @Test
    void testUpdate_ShouldThrowDataNotFound_WhenNotFound() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.update(99L, request))
                .isInstanceOf(DataNotFound.class)
                .hasMessage("Department not found");

        verify(departmentRepository, never()).save(any());
    }

    @Test
    void testDelete_ShouldRemoveDepartment() {
        Department department = Department.builder().id(1L).name("HR").build();
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        departmentService.delete(1L);

        verify(departmentRepository, times(1)).delete(department);
    }

    @Test
    void testDelete_ShouldThrowDataNotFound_WhenNotFound() {
        when(departmentRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.delete(10L))
                .isInstanceOf(DataNotFound.class)
                .hasMessage("Department not found");

        verify(departmentRepository, never()).delete(any());
    }

    @Test
    void testGetById_ShouldReturnDepartmentResponse() {
        Department department = Department.builder()
                .id(1L)
                .name("Finance")
                .description("Finance Dept")
                .managerId(5L)
                .isActive(true)
                .employeeCount(20)
                .build();

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));

        DepartmentResponse response = departmentService.getById(1L);

        assertThat(response.getName()).isEqualTo("Finance");
        assertThat(response.getDescription()).isEqualTo("Finance Dept");
        assertThat(response.getManagerId()).isEqualTo(5L);
        assertThat(response.getIsActive()).isTrue();
        assertThat(response.getEmployeeCount()).isEqualTo(20);
    }

    @Test
    void testGetById_ShouldThrowDataNotFound_WhenNotFound() {
        when(departmentRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.getById(2L))
                .isInstanceOf(DataNotFound.class)
                .hasMessage("Department not found");
    }

    @Test
    void testGetAll_ShouldReturnListOfResponses() {
        Department d1 = Department.builder().id(1L).name("IT").build();
        Department d2 = Department.builder().id(2L).name("HR").build();

        when(departmentRepository.findAll()).thenReturn(Arrays.asList(d1, d2));

        List<DepartmentResponse> responses = departmentService.getAll();

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getName()).isEqualTo("IT");
        assertThat(responses.get(1).getName()).isEqualTo("HR");
    }
}
