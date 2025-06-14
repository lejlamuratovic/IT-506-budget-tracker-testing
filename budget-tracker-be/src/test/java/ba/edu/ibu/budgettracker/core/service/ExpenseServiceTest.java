package ba.edu.ibu.budgettracker.core.service;

import ba.edu.ibu.budgettracker.core.model.Budget;
import ba.edu.ibu.budgettracker.core.model.Category;
import ba.edu.ibu.budgettracker.core.model.Expense;
import ba.edu.ibu.budgettracker.core.model.User;
import ba.edu.ibu.budgettracker.core.repository.BudgetRepository;
import ba.edu.ibu.budgettracker.core.repository.ExpenseRepository;
import ba.edu.ibu.budgettracker.rest.dto.CategoryChartDto;
import ba.edu.ibu.budgettracker.rest.dto.ExpenseDto;
import ba.edu.ibu.budgettracker.rest.dto.ExpenseRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private UserService userService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private ExpenseService expenseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllExpenses_Success() {
        // Given
        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(1L);

        Expense expense1 = new Expense("Expense 1", 100.0, new Date(), category, user);
        expense1.setId(1L);
        Expense expense2 = new Expense("Expense 2", 200.0, new Date(), category, user);
        expense2.setId(2L);

        when(expenseRepository.findAll()).thenReturn(List.of(expense1, expense2));

        // When
        List<ExpenseDto> result = expenseService.getAllExpenses();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Expense 1");
        assertThat(result.get(1).getTitle()).isEqualTo("Expense 2");
    }

    @Test
    void testGetExpenseById_Success() {
        // Given
        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(1L);

        Expense expense = new Expense("Expense 1", 100.0, new Date(), category, user);
        expense.setId(1L);

        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));

        // When
        Optional<ExpenseDto> result = expenseService.getExpenseById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Expense 1");
        assertThat(result.get().getAmount()).isEqualTo(100.0);
    }

    @Test
    void testGetExpenseById_NotFound() {
        // Given
        when(expenseRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<ExpenseDto> result = expenseService.getExpenseById(1L);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void testCreateExpense_Success() {
        // Given
        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(1L);

        Budget budget = new Budget();
        budget.setId(1L);
        budget.setRemaining(500.0);

        ExpenseRequest request = new ExpenseRequest(
                "Test Expense",
                100.0,
                new Date(),
                1L,
                1L
        );

        when(userService.getUserEntityById(eq(request.getUserId()))).thenReturn(user);
        when(categoryService.getCategoryEntityById(eq(request.getCategoryId()))).thenReturn(category);
        when(budgetService.getBudgetByUserAndMonthAndYear(
                eq(request.getUserId()),
                eq(extractMonth(request.getDate())),
                eq(extractYear(request.getDate()))
        )).thenReturn(Optional.of(budget));
        when(expenseRepository.save(any(Expense.class))).thenAnswer(invocation -> {
            Expense savedExpense = invocation.getArgument(0);
            savedExpense.setId(1L);
            return savedExpense;
        });

        // When
        ExpenseDto result = expenseService.createExpense(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getAmount()).isEqualTo(100.0);
        assertThat(result.getTitle()).isEqualTo("Test Expense");
    }

    @Test
    void testUpdateExpense_Success() {
        // Given
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setAmount(100.0);
        expense.setDate(new Date());
        expense.setTitle("Original Expense");

        User user = new User();
        user.setId(1L);
        expense.setUser(user);

        Category category = new Category();
        category.setId(1L);
        expense.setCategory(category);

        Budget budget = new Budget();
        budget.setId(1L);
        budget.setRemaining(500.0);

        ExpenseRequest request = new ExpenseRequest(
                "Updated Expense",
                150.0,
                new Date(),
                1L,
                1L
        );

        Integer originalMonth = extractMonth(expense.getDate());
        Integer originalYear = extractYear(expense.getDate());

        when(expenseRepository.findById(eq(expense.getId()))).thenReturn(Optional.of(expense));
        when(categoryService.getCategoryEntityById(eq(request.getCategoryId()))).thenReturn(category);
        when(budgetService.getBudgetByUserAndMonthAndYear(
                eq(user.getId()),
                eq(originalMonth),
                eq(originalYear)
        )).thenReturn(Optional.of(budget));
        when(expenseRepository.save(any(Expense.class))).thenAnswer(invocation -> {
            Expense savedExpense = invocation.getArgument(0);
            savedExpense.setId(1L);
            return savedExpense;
        });

        // When
        ExpenseDto result = expenseService.updateExpense(expense.getId(), request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Updated Expense");
        assertThat(result.getAmount()).isEqualTo(150.0);
    }

    @Test
    void testDeleteExpense_Success() {
        // Given
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setAmount(100.0);
        expense.setDate(new Date());

        User user = new User();
        user.setId(1L);
        expense.setUser(user);

        Budget budget = new Budget();
        budget.setId(1L);
        budget.setRemaining(500.0);

        when(expenseRepository.findById(eq(expense.getId()))).thenReturn(Optional.of(expense));
        when(budgetService.getBudgetByUserAndMonthAndYear(
                eq(user.getId()),
                eq(extractMonth(expense.getDate())),
                eq(extractYear(expense.getDate()))
        )).thenReturn(Optional.of(budget));

        // When
        expenseService.deleteExpense(expense.getId());

        // Then
        verify(expenseRepository).deleteById(expense.getId());
        verify(budgetRepository).save(any(Budget.class));
    }

    @Test
    void testFilterExpenses_Success() {
        // Given
        Long userId = 1L;

        // Mocking categories
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Category 1");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");

        // Mocking expenses
        Expense expense1 = new Expense("Expense 1", 100.0, new Date(), category1, null);
        Expense expense2 = new Expense("Expense 2", 200.0, new Date(), category2, null);

        User user = new User();
        user.setId(userId);
        expense1.setUser(user);
        expense2.setUser(user);

        when(expenseRepository.findByUserId(userId)).thenReturn(List.of(expense1, expense2));

        // When
        List<ExpenseDto> result = expenseService.filterExpenses(userId, null, null, null, null, null, null, null);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Expense 1");
        assertThat(result.get(1).getTitle()).isEqualTo("Expense 2");
    }

    private int extractMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1; // Add 1 because January = 0
    }

    private int extractYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    @Test
    void testGetCategoryChartData_NoExpenses() {
        // Given
        Long userId = 1L;
        when(expenseRepository.findByUserId(userId)).thenReturn(List.of());

        // When
        List<CategoryChartDto> result = expenseService.getCategoryChartData(userId, null, null);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void testGetExpenseById_NotFoundThrowsException() {
        // Given
        Long expenseId = 1L;
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.empty());

        // When & Then
        Optional<ExpenseDto> result = expenseService.getExpenseById(expenseId);
        assertThat(result).isEmpty();
    }

    @Test
    void testFilterExpenses_WithCategoryAndDateRange() {
        // Given
        Long userId = 1L;
        Long categoryId = 1L;
        Date startDate = new Date(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L); // 1 week ago
        Date endDate = new Date();

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Category 1");

        Expense expense = new Expense("Expense 1", 150.0, startDate, category, null);

        User user = new User();
        user.setId(userId);

        expense.setUser(user);

        when(expenseRepository.findByUserId(userId)).thenReturn(List.of(expense));

        // When
        List<ExpenseDto> result = expenseService.filterExpenses(userId, categoryId, startDate, endDate, null, null, null, null);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Expense 1");
    }
}
