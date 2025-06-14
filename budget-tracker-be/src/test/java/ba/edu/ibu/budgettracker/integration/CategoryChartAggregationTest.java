package ba.edu.ibu.budgettracker.integration;

import ba.edu.ibu.budgettracker.core.model.Budget;
import ba.edu.ibu.budgettracker.core.model.Category;
import ba.edu.ibu.budgettracker.core.model.User;
import ba.edu.ibu.budgettracker.core.repository.BudgetRepository;
import ba.edu.ibu.budgettracker.core.repository.CategoryRepository;
import ba.edu.ibu.budgettracker.core.repository.UserRepository;
import ba.edu.ibu.budgettracker.core.service.ExpenseService;
import ba.edu.ibu.budgettracker.rest.dto.CategoryChartDto;
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
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryChartAggregationIntegrationTest {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    private Long userId;
    private Long foodCatId;
    private Long transportCatId;

    @BeforeEach
    void setUp() {
        // User
        User user = new User("chart_user@test.com");
        userRepository.save(user);
        userId = user.getId();

        // Categories
        Category food = new Category("Food");
        Category transport = new Category("Transport");
        categoryRepository.save(food);
        categoryRepository.save(transport);
        foodCatId = food.getId();
        transportCatId = transport.getId();

        // Budget so that expenses can be created (1000 each)
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        Budget budget = new Budget(1000.0, 1000.0, month, year, user);
        budgetRepository.save(budget);

        // Expenses: Food 10 + 20, Transport 5
        expenseService.createExpense(new ExpenseRequest("Sandwich", 10.0, new Date(), foodCatId, userId));
        expenseService.createExpense(new ExpenseRequest("Dinner", 20.0, new Date(), foodCatId, userId));
        expenseService.createExpense(new ExpenseRequest("Bus", 5.0, new Date(), transportCatId, userId));
    }

    @Test
    void categoryChartDataShouldAggregateTotalsAndCounts() {
        var chartData = expenseService.getCategoryChartData(userId, null, null);
        assertThat(chartData).hasSize(2);

        // Convert to map by name for easier assertions
        Map<String, CategoryChartDto> map = chartData.stream()
                .collect(Collectors.toMap(CategoryChartDto::getCategoryName, c -> c));

        CategoryChartDto foodDto = map.get("Food");
        assertThat(foodDto).isNotNull();
        assertThat(foodDto.getExpenseCount()).isEqualTo(2);
        assertThat(foodDto.getTotalAmount()).isEqualTo(30.0);

        CategoryChartDto transportDto = map.get("Transport");
        assertThat(transportDto).isNotNull();
        assertThat(transportDto.getExpenseCount()).isEqualTo(1);
        assertThat(transportDto.getTotalAmount()).isEqualTo(5.0);
    }
} 
