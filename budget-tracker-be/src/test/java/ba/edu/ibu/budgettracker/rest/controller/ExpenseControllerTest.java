package ba.edu.ibu.budgettracker.rest.controller;

import ba.edu.ibu.budgettracker.core.service.ExpenseService;
import ba.edu.ibu.budgettracker.rest.dto.CategoryChartDto;
import ba.edu.ibu.budgettracker.rest.dto.ExpenseDto;
import ba.edu.ibu.budgettracker.rest.dto.ExpenseRequest;
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
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ExpenseControllerTest {

    @Mock
    private ExpenseService expenseService;

    @InjectMocks
    private ExpenseController expenseController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(expenseController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateExpense_Success() throws Exception {
        ExpenseRequest request = new ExpenseRequest();
        request.setTitle("Test Expense");
        request.setAmount(100.0);
        request.setDate(new Date());
        request.setCategoryId(1L);
        request.setUserId(1L);

        ExpenseDto expenseDto = new ExpenseDto();
        expenseDto.setId(1L);
        expenseDto.setTitle(request.getTitle());
        expenseDto.setAmount(request.getAmount());
        expenseDto.setDate(request.getDate());
        expenseDto.setCategoryId(request.getCategoryId());
        expenseDto.setUserId(request.getUserId());

        when(expenseService.createExpense(any(ExpenseRequest.class))).thenReturn(expenseDto);

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(expenseDto.getId()))
                .andExpect(jsonPath("$.title").value(expenseDto.getTitle()))
                .andExpect(jsonPath("$.amount").value(expenseDto.getAmount()))
                .andExpect(jsonPath("$.categoryId").value(expenseDto.getCategoryId()))
                .andExpect(jsonPath("$.userId").value(expenseDto.getUserId()));
    }

    @Test
    void testUpdateExpense_Success() throws Exception {
        Long expenseId = 1L;
        ExpenseRequest request = new ExpenseRequest();
        request.setTitle("Updated Expense");
        request.setAmount(200.0);
        request.setDate(new Date());
        request.setCategoryId(1L);
        request.setUserId(1L);

        ExpenseDto updatedExpense = new ExpenseDto(
                expenseId,
                "Updated Expense",
                200.0,
                new Date(),
                1L,
                1L
        );

        when(expenseService.updateExpense(eq(expenseId), any(ExpenseRequest.class)))
                .thenReturn(updatedExpense);

        mockMvc.perform(put("/api/expenses/{id}", expenseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Expense"))
                .andExpect(jsonPath("$.amount").value(200.0))
                .andExpect(jsonPath("$.categoryId").value(1))
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    void testUpdateExpense_NotFound() throws Exception {
        Long nonExistentExpenseId = -1L; // Non-existent ID
        ExpenseRequest request = new ExpenseRequest();
        request.setTitle("Non-existent Expense");
        request.setAmount(100.0);
        request.setDate(new Date());
        request.setCategoryId(1L);
        request.setUserId(1L);

        // Mock the service to throw an exception for the non-existent expense ID
        when(expenseService.updateExpense(eq(nonExistentExpenseId), any(ExpenseRequest.class)))
                .thenThrow(new IllegalArgumentException("Expense not found with id: " + nonExistentExpenseId));

        // Perform the test and verify the response
        mockMvc.perform(put("/api/expenses/{id}", nonExistentExpenseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound()); // Expect 404 status
    }

    @Test
    void testDeleteExpense_Success() throws Exception {
        Long expenseId = 1L;

        mockMvc.perform(delete("/api/expenses/{id}", expenseId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteExpense_NotFound() throws Exception {
        Long expenseId = 1L;

        // Use doThrow for mocking void methods
        doThrow(new IllegalArgumentException("Expense not found"))
                .when(expenseService).deleteExpense(eq(expenseId));

        mockMvc.perform(delete("/api/expenses/{id}", expenseId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFilterExpenses_Success() throws Exception {
        Long userId = 1L;
        ExpenseDto expense1 = new ExpenseDto(1L, "Groceries", 100.0, new Date(), 1L, userId);
        ExpenseDto expense2 = new ExpenseDto(2L, "Rent", 500.0, new Date(), 2L, userId);

        when(expenseService.filterExpenses(eq(userId), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(Arrays.asList(expense1, expense2));

        mockMvc.perform(get("/api/expenses/filter")
                        .param("userId", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Groceries"))
                .andExpect(jsonPath("$[1].title").value("Rent"));
    }

    @Test
    void testGetCategoryChartData_Success() throws Exception {
        Long userId = 1L;
        CategoryChartDto chartData1 = new CategoryChartDto("Groceries", 5L);
        CategoryChartDto chartData2 = new CategoryChartDto("Rent", 2L);

        when(expenseService.getCategoryChartData(eq(userId), any(), any()))
                .thenReturn(Arrays.asList(chartData1, chartData2));

        mockMvc.perform(get("/api/expenses/chart-data")
                        .param("userId", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].categoryName").value("Groceries"))
                .andExpect(jsonPath("$[1].categoryName").value("Rent"));
    }
}
