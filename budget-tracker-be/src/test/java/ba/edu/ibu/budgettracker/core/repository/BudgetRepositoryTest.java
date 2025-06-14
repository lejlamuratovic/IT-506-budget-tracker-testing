package ba.edu.ibu.budgettracker.core.repository;

import ba.edu.ibu.budgettracker.core.model.Budget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class BudgetRepositoryTest {

    @Mock
    private BudgetRepository budgetRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUserIdAndMonthAndYear() {
        // Given
        Long userId = 1L;
        int month = 3;
        int year = 2024;

        Budget budget = new Budget();
        budget.setId(1L);
        budget.setAmount(1000.0);
        budget.setMonth(month);
        budget.setYear(year);

        when(budgetRepository.findByUserIdAndMonthAndYear(userId, month, year)).thenReturn(Optional.of(budget));

        // When
        Optional<Budget> result = budgetRepository.findByUserIdAndMonthAndYear(userId, month, year);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getAmount()).isEqualTo(1000.0);
    }
}
