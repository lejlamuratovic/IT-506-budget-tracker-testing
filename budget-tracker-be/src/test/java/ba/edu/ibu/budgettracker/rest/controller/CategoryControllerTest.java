package ba.edu.ibu.budgettracker.rest.controller;

import ba.edu.ibu.budgettracker.core.service.CategoryService;
import ba.edu.ibu.budgettracker.rest.dto.CategoryDto;
import ba.edu.ibu.budgettracker.rest.dto.CategoryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void testGetAllCategories_Success() throws Exception {
        // Given
        List<CategoryDto> mockCategories = Arrays.asList(
                new CategoryDto(1L, "Food"),
                new CategoryDto(2L, "Transport")
        );

        when(categoryService.getAllCategories()).thenReturn(mockCategories);

        // When & Then
        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Food"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Transport"));
    }

    @Test
    void testGetCategoryById_Success() throws Exception {
        // Given
        Long categoryId = 1L;
        CategoryRequest mockCategory = new CategoryRequest("Food");

        when(categoryService.getCategoryById(categoryId)).thenReturn(Optional.of(mockCategory));

        // When & Then
        mockMvc.perform(get("/api/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Food"));
    }

    @Test
    void testGetCategoryById_NotFound() throws Exception {
        // Given
        Long categoryId = 1L;

        when(categoryService.getCategoryById(categoryId)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
