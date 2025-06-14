package ba.edu.ibu.budgettracker.core.service;

import ba.edu.ibu.budgettracker.core.api.EmailClient;
import ba.edu.ibu.budgettracker.core.model.Budget;
import ba.edu.ibu.budgettracker.rest.dto.CategoryChartDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailService {

    private final EmailClient emailClient;
    private final BudgetService budgetService;
    private final ExpenseService expenseService;

    public EmailService(EmailClient emailClient, BudgetService budgetService, ExpenseService expenseService) {
        this.emailClient = emailClient;
        this.budgetService = budgetService;
        this.expenseService = expenseService;
    }

    public void sendUserReportEmail(String to, Long userId, int month, int year) {
        // Fetch user's budget and remaining status
        Budget budgetDto = budgetService.getBudgetByUserAndMonthAndYear(userId, month, year)
                .orElseThrow(() -> new IllegalArgumentException("No budget found for the specified user, month, and year."));

        // Fetch user's category expense details
        List<CategoryChartDto> categoryData = expenseService.getCategoryChartData(userId, null, null);

        // Build email body
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("Budget Tracker Report\n\n");
        emailBody.append("Month: ").append(month).append("/").append(year).append("\n");
        emailBody.append("Budget: ").append(budgetDto.getAmount()).append("\n");
        emailBody.append("Remaining: ").append(budgetDto.getRemaining()).append("\n\n");
        emailBody.append("Category Expenses:\n");

        if (categoryData.isEmpty()) {
            emailBody.append("No expenses found.\n");
        } else {
            emailBody.append(categoryData.stream()
                    .map(category -> "Category: " + category.getCategoryName() +
                            ", Expenses: " + category.getExpenseCount())
                    .collect(Collectors.joining("\n")));
        }

        // Validate and send the email
        if (!emailClient.validateEmail(to)) {
            throw new IllegalArgumentException("Invalid email address: " + to);
        }

        try {
            emailClient.sendEmail(to, emailBody.toString());
        } catch (Exception ex) {
            throw new RuntimeException("Failed to send email: " + ex.getMessage(), ex);
        }
    }
}
