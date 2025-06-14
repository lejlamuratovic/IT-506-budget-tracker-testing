package ba.edu.ibu.budgettracker.integration;

import ba.edu.ibu.budgettracker.core.model.Budget;
import ba.edu.ibu.budgettracker.core.model.Category;
import ba.edu.ibu.budgettracker.core.model.User;
import ba.edu.ibu.budgettracker.core.repository.BudgetRepository;
import ba.edu.ibu.budgettracker.core.repository.CategoryRepository;
import ba.edu.ibu.budgettracker.core.repository.ExpenseRepository;
import ba.edu.ibu.budgettracker.core.repository.UserRepository;
import ba.edu.ibu.budgettracker.core.service.ExpenseService;
import ba.edu.ibu.budgettracker.rest.dto.ExpenseDto;
import ba.edu.ibu.budgettracker.rest.dto.ExpenseRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExpenseUpdateIntegrationTest {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Long userId;
    private Long categoryId;
    private Long budgetId;
    private int month;
    private int year;

    @BeforeEach
    void setUp() {
        // Create user
        User user = new User("expense_update@test.com");
        userRepository.save(user);
        userId = user.getId();

        // Create category
        Category category = new Category("Groceries");
        categoryRepository.save(category);
        categoryId = category.getId();

        // Current month/year
        Calendar cal = Calendar.getInstance();
        month = cal.get(Calendar.MONTH) + 1;
        year = cal.get(Calendar.YEAR);

        // Create budget
        Budget budget = new Budget(1000.0, 1000.0, month, year, user);
        budgetRepository.save(budget);
        budgetId = budget.getId();
    }

    @Test
    void whenExpenseUpdated_budgetRemainingShouldAdjustAccordingly() {
        // 1. Create initial expense of 100
        ExpenseRequest createReq = new ExpenseRequest("Breakfast", 100.0, new Date(), categoryId, userId);
        ExpenseDto expense = expenseService.createExpense(createReq);

        Budget afterCreate = budgetRepository.findById(budgetId).orElseThrow();
        assertThat(afterCreate.getRemaining()).isEqualTo(900.0);

        // 2. Update expense to a lower amount (50) -> remaining should increase by 50 to 950
        ExpenseRequest updateLower = new ExpenseRequest("Breakfast", 50.0, new Date(), categoryId, userId);
        expenseService.updateExpense(expense.getId(), updateLower);

        Budget afterLowerUpdate = budgetRepository.findById(budgetId).orElseThrow();
        assertThat(afterLowerUpdate.getRemaining()).isEqualTo(950.0);

        // 3. Update expense to a higher amount (200) -> remaining should decrease by 150 to 800
        ExpenseRequest updateHigher = new ExpenseRequest("Breakfast", 200.0, new Date(), categoryId, userId);
        expenseService.updateExpense(expense.getId(), updateHigher);

        Budget afterHigherUpdate = budgetRepository.findById(budgetId).orElseThrow();
        assertThat(afterHigherUpdate.getRemaining()).isEqualTo(800.0);
    }
} 

