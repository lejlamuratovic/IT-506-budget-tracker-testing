package ba.edu.ibu.budgettracker.rest.controller;

import ba.edu.ibu.budgettracker.core.model.Budget;
import ba.edu.ibu.budgettracker.core.model.User;
import ba.edu.ibu.budgettracker.core.service.BudgetService;
import ba.edu.ibu.budgettracker.rest.dto.BudgetDto;
import ba.edu.ibu.budgettracker.rest.dto.BudgetRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BudgetControllerTest {

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private BudgetController budgetController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(budgetController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetBudget_Success() throws Exception {
        // Given
        Long userId = 1L;
        Integer month = 1;
        Integer year = 2024;

        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setEmail("test@example.com");

        Budget mockBudget = new Budget(1000.0, 800.0, month, year, mockUser);
        mockBudget.setId(1L);

        when(budgetService.getBudgetByUserAndMonthAndYear(userId, month, year)).thenReturn(Optional.of(mockBudget));

        // When & Then
        mockMvc.perform(get("/api/budgets/user")
                        .param("userId", userId.toString())
                        .param("month", month.toString())
                        .param("year", year.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockBudget.getId()))
                .andExpect(jsonPath("$.amount").value(mockBudget.getAmount()))
                .andExpect(jsonPath("$.remaining").value(mockBudget.getRemaining()))
                .andExpect(jsonPath("$.month").value(mockBudget.getMonth()))
                .andExpect(jsonPath("$.year").value(mockBudget.getYear()))
                .andExpect(jsonPath("$.userId").value(mockUser.getId()));
    }

    @Test
    void testGetBudget_NotFound() throws Exception {
        // Given
        Long userId = 1L;
        Integer month = 1;
        Integer year = 2024;

        when(budgetService.getBudgetByUserAndMonthAndYear(userId, month, year)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/budgets/user")
                        .param("userId", userId.toString())
                        .param("month", month.toString())
                        .param("year", year.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void testCreateBudget_Success() throws Exception {
        // Given
        BudgetRequest request = new BudgetRequest(1000.0, 1, 2024, 1L);
        BudgetDto mockBudgetDto = new BudgetDto(1L, 1000.0, 1000.0, 1, 2024, 1L);

        when(budgetService.createBudget(any(BudgetRequest.class))).thenReturn(mockBudgetDto);

        // When & Then
        mockMvc.perform(post("/api/budgets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(mockBudgetDto.getId()))
                .andExpect(jsonPath("$.amount").value(mockBudgetDto.getAmount()))
                .andExpect(jsonPath("$.remaining").value(mockBudgetDto.getRemaining()))
                .andExpect(jsonPath("$.month").value(mockBudgetDto.getMonth()))
                .andExpect(jsonPath("$.year").value(mockBudgetDto.getYear()))
                .andExpect(jsonPath("$.userId").value(mockBudgetDto.getUserId()));
    }

    @Test
    void testUpdateBudget_Success() throws Exception {
        // Given
        Long id = 1L;
        BudgetRequest request = new BudgetRequest(1200.0, 2, 2024, 1L);
        BudgetDto updatedBudgetDto = new BudgetDto(1L, 1200.0, 1200.0, 2, 2024, 1L);

        when(budgetService.updateBudget(eq(id), any(BudgetRequest.class))).thenReturn(updatedBudgetDto);

        // When & Then
        mockMvc.perform(put("/api/budgets/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedBudgetDto.getId()))
                .andExpect(jsonPath("$.amount").value(updatedBudgetDto.getAmount()))
                .andExpect(jsonPath("$.remaining").value(updatedBudgetDto.getRemaining()))
                .andExpect(jsonPath("$.month").value(updatedBudgetDto.getMonth()))
                .andExpect(jsonPath("$.year").value(updatedBudgetDto.getYear()))
                .andExpect(jsonPath("$.userId").value(updatedBudgetDto.getUserId()));
    }

    @Test
    void testUpdateBudget_NotFound() throws Exception {
        // Given
        Long id = 1L;
        BudgetRequest request = new BudgetRequest(1200.0, 2, 2024, 1L);

        when(budgetService.updateBudget(eq(id), any(BudgetRequest.class)))
                .thenThrow(new IllegalArgumentException("Budget not found"));

        // When & Then
        mockMvc.perform(put("/api/budgets/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteBudget_Success() throws Exception {
        // Given
        Long id = 1L;

        doNothing().when(budgetService).deleteBudget(id);

        // When & Then
        mockMvc.perform(delete("/api/budgets/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteBudget_NotFound() throws Exception {
        // Given
        Long id = 1L;

        doThrow(new IllegalArgumentException("Budget not found")).when(budgetService).deleteBudget(id);

        // When & Then
        mockMvc.perform(delete("/api/budgets/{id}", id))
                .andExpect(status().isNotFound());
    }
}
