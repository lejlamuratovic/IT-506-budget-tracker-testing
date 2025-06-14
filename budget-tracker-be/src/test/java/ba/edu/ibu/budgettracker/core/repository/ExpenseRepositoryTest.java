package ba.edu.ibu.budgettracker.core.repository;

import ba.edu.ibu.budgettracker.core.model.Expense;
import ba.edu.ibu.budgettracker.core.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ExpenseRepositoryTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUserId() {
        // Given
        Long userId = 1L;
        Expense expense1 = new Expense();
        expense1.setId(1L);
        expense1.setAmount(150.0);

        Expense expense2 = new Expense();
        expense2.setId(2L);
        expense2.setAmount(200.0);

        when(expenseRepository.findByUserId(userId)).thenReturn(List.of(expense1, expense2));

        // When
        List<Expense> result = expenseRepository.findByUserId(userId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getAmount()).isEqualTo(150.0);
        assertThat(result.get(1).getAmount()).isEqualTo(200.0);
    }

    @Test
    void testFindByUserIdAndMonthAndYear() {
        // Given
        Long userId = 1L;
        int month = 3;
        int year = 2024;

        Expense expense = new Expense();
        expense.setId(1L);
        expense.setDate(new Date(2024, Calendar.APRIL, 10));
        expense.setAmount(150.0);

        when(expenseRepository.findByUserIdAndMonthAndYear(userId, month, year)).thenReturn(List.of(expense));

        // When
        List<Expense> result = expenseRepository.findByUserIdAndMonthAndYear(userId, month, year);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAmount()).isEqualTo(150.0);
    }
}
