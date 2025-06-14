package ba.edu.ibu.budgettracker.core.repository;

import ba.edu.ibu.budgettracker.core.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class CategoryRepositoryTest {

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        // Given
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Groceries");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // When
        Optional<Category> result = categoryRepository.findById(categoryId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Groceries");
    }
}
