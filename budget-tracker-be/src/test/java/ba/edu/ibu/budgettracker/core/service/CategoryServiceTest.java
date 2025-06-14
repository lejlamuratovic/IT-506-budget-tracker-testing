package ba.edu.ibu.budgettracker.core.service;

import ba.edu.ibu.budgettracker.core.model.Category;
import ba.edu.ibu.budgettracker.core.repository.CategoryRepository;
import ba.edu.ibu.budgettracker.rest.dto.CategoryDto;
import ba.edu.ibu.budgettracker.rest.dto.CategoryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategories() {
        // Given
        Category category1 = new Category(1L, "Groceries");
        Category category2 = new Category(2L, "Transportation");
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        // When
        List<CategoryDto> categories = categoryService.getAllCategories();

        // Then
        assertThat(categories).hasSize(2);
        assertThat(categories.get(0).getName()).isEqualTo("Groceries");
        assertThat(categories.get(1).getName()).isEqualTo("Transportation");
    }

    @Test
    void testGetCategoryById_Found() {
        // Given
        Long categoryId = 1L;
        Category category = new Category(categoryId, "Entertainment");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // When
        Optional<CategoryRequest> result = categoryService.getCategoryById(categoryId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Entertainment");
    }

    @Test
    void testGetCategoryById_NotFound() {
        // Given
        Long categoryId = 999L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When
        Optional<CategoryRequest> result = categoryService.getCategoryById(categoryId);

        // Then
        assertThat(result).isNotPresent();
    }

    @Test
    void testGetCategoryEntityById_Found() {
        // Given
        Long categoryId = 1L;
        Category category = new Category(categoryId, "Health");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // When
        Category result = categoryService.getCategoryEntityById(categoryId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(categoryId);
        assertThat(result.getName()).isEqualTo("Health");
    }

    @Test
    void testGetCategoryEntityById_NotFound() {
        // Given
        Long categoryId = 999L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When / Then
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> categoryService.getCategoryEntityById(categoryId));
        assertThat(exception.getMessage()).isEqualTo("Category not found with id: " + categoryId);
    }
}
