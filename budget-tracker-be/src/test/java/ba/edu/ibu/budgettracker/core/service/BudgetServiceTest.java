package ba.edu.ibu.budgettracker.core.service;

import ba.edu.ibu.budgettracker.core.model.Budget;
import ba.edu.ibu.budgettracker.core.model.Expense;
import ba.edu.ibu.budgettracker.core.model.User;
import ba.edu.ibu.budgettracker.core.repository.BudgetRepository;
import ba.edu.ibu.budgettracker.core.repository.ExpenseRepository;
import ba.edu.ibu.budgettracker.rest.dto.BudgetDto;
import ba.edu.ibu.budgettracker.rest.dto.BudgetRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BudgetServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private BudgetService budgetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBudget() {
        // Given
        BudgetRequest request = new BudgetRequest(1000.0, 1, 2024, 1L);
        User user = new User();
        user.setId(1L);

        Budget budget = new Budget(1000.0, 1000.0, 1, 2024, user);
        budget.setId(1L);

        when(userService.getUserEntityById(1L)).thenReturn(user);
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        // When
        BudgetDto result = budgetService.createBudget(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(1000.0);
        assertThat(result.getRemaining()).isEqualTo(1000.0);
        verify(budgetRepository, times(1)).save(any(Budget.class));
    }

    @Test
    void testGetBudgetByUserAndMonthAndYear() {
        // Given
        Long userId = 1L;
        int month = 1;
        int year = 2024;

        Budget budget = new Budget();
        budget.setId(1L);
        budget.setAmount(1000.0);

        when(budgetRepository.findByUserIdAndMonthAndYear(userId, month, year)).thenReturn(Optional.of(budget));

        // When
        Optional<Budget> result = budgetService.getBudgetByUserAndMonthAndYear(userId, month, year);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getAmount()).isEqualTo(1000.0);
        verify(budgetRepository, times(1)).findByUserIdAndMonthAndYear(userId, month, year);
    }

    @Test
    void testUpdateBudget() {
        // Given
        Long budgetId = 1L;
        User user = new User();
        user.setId(1L);

        Budget budget = new Budget(1000.0, 800.0, 1, 2024, user);

        Expense expense1 = new Expense("Expense 1", 100.0, new Date(), null, user);
        Expense expense2 = new Expense("Expense 2", 200.0, new Date(), null, user);

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(budget));
        when(userService.getUserEntityById(1L)).thenReturn(user);
        when(expenseRepository.findByUserIdAndMonthAndYear(1L, 1, 2024))
                .thenReturn(List.of(expense1, expense2));
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);

        // When
        BudgetDto result = budgetService.updateBudget(budgetId, new BudgetRequest(1500.0, 1, 2024, 1L));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(1500.0);
        assertThat(result.getRemaining()).isEqualTo(1200.0); // Adjusted remaining
    }

    @Test
    void testUpdateBudgetNotFound() {
        // Given
        Long budgetId = 1L;
        BudgetRequest request = new BudgetRequest(1200.0, 2, 2024, 1L);

        when(budgetRepository.findById(budgetId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> budgetService.updateBudget(budgetId, request));
    }

    @Test
    void testDeleteBudget() {
        // Given
        Long budgetId = 1L;

        when(budgetRepository.existsById(budgetId)).thenReturn(true);

        // When
        budgetService.deleteBudget(budgetId);

        // Then
        verify(budgetRepository, times(1)).deleteById(budgetId);
    }

    @Test
    void testDeleteBudgetNotFound() {
        // Given
        Long budgetId = 1L;

        when(budgetRepository.existsById(budgetId)).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> budgetService.deleteBudget(budgetId));
    }
}
