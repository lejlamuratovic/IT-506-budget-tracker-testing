package ba.edu.ibu.budgettracker.core.service;

import ba.edu.ibu.budgettracker.core.api.EmailClient;
import ba.edu.ibu.budgettracker.core.model.Budget;
import ba.edu.ibu.budgettracker.rest.dto.CategoryChartDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private EmailClient emailClient;

    @Mock
    private BudgetService budgetService;

    @Mock
    private ExpenseService expenseService;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendUserReportEmail_Success() {
        // Given
        String to = "test@example.com";
        Long userId = 1L;
        int month = 1;
        int year = 2024;

        Budget budget = new Budget();
        budget.setAmount(1000.0);
        budget.setRemaining(200.0);

        List<CategoryChartDto> categoryData = List.of(
                new CategoryChartDto("Groceries", 300.0, 3L, List.of()),
                new CategoryChartDto("Transport", 200.0, 2L, List.of())
        );

        when(budgetService.getBudgetByUserAndMonthAndYear(userId, month, year)).thenReturn(Optional.of(budget));
        when(expenseService.getCategoryChartData(userId, null, null)).thenReturn(categoryData);
        when(emailClient.validateEmail(to)).thenReturn(true);

        // When
        emailService.sendUserReportEmail(to, userId, month, year);

        // Then
        verify(emailClient).sendEmail(eq(to), contains("Budget Tracker Report"));
        verify(emailClient).sendEmail(eq(to), contains("Groceries"));
        verify(emailClient).sendEmail(eq(to), contains("Transport"));
    }

    @Test
    void testSendUserReportEmail_BudgetNotFound() {
        // Given
        String to = "test@example.com";
        Long userId = 1L;
        int month = 1;
        int year = 2024;

        when(budgetService.getBudgetByUserAndMonthAndYear(userId, month, year)).thenReturn(Optional.empty());

        // When / Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> emailService.sendUserReportEmail(to, userId, month, year))
                .withMessage("No budget found for the specified user, month, and year.");

        verifyNoInteractions(emailClient);
    }

    @Test
    void testSendUserReportEmail_InvalidEmail() {
        // Given
        String to = "invalid-email";
        Long userId = 1L;
        int month = 1;
        int year = 2024;

        Budget budget = new Budget();
        budget.setAmount(1000.0);
        budget.setRemaining(200.0);

        when(budgetService.getBudgetByUserAndMonthAndYear(userId, month, year)).thenReturn(Optional.of(budget));
        when(expenseService.getCategoryChartData(userId, null, null)).thenReturn(List.of());
        when(emailClient.validateEmail(to)).thenReturn(false);

        // When / Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> emailService.sendUserReportEmail(to, userId, month, year))
                .withMessage("Invalid email address: " + to);

        verify(emailClient, never()).sendEmail(anyString(), anyString());
    }

    @Test
    void testSendUserReportEmail_EmailClientThrowsException() {
        // Given
        String to = "test@example.com";
        Long userId = 1L;
        int month = 1;
        int year = 2024;

        Budget budget = new Budget();
        budget.setAmount(1000.0);
        budget.setRemaining(200.0);

        when(budgetService.getBudgetByUserAndMonthAndYear(userId, month, year)).thenReturn(Optional.of(budget));
        when(expenseService.getCategoryChartData(userId, null, null)).thenReturn(List.of());
        when(emailClient.validateEmail(to)).thenReturn(true);

        doThrow(new RuntimeException("SMTP error")).when(emailClient).sendEmail(anyString(), anyString());

        // When / Then
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> emailService.sendUserReportEmail(to, userId, month, year))
                .withMessageContaining("Failed to send email");

        verify(emailClient).sendEmail(eq(to), contains("Budget Tracker Report"));
    }
}