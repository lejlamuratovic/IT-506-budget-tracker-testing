package ba.edu.ibu.budgettracker.rest.dto;

import java.util.List;

public class CategoryChartDto {
    private String categoryName;
    private double totalAmount;
    private long expenseCount; // Number of expenses in the category
    private List<ExpenseDto> expenses;

    public CategoryChartDto(String categoryName, double totalAmount, long expenseCount, List<ExpenseDto> expenses) {
        this.categoryName = categoryName;
        this.totalAmount = totalAmount;
        this.expenseCount = expenseCount;
        this.expenses = expenses;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<ExpenseDto> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseDto> expenses) {
        this.expenses = expenses;
    }

    public long getExpenseCount() {
        return expenseCount;
    }

    public void setExpenseCount(long expenseCount) {
        this.expenseCount = expenseCount;
    }
}
