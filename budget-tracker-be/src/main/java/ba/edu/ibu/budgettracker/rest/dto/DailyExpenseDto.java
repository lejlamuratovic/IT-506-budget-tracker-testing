package ba.edu.ibu.budgettracker.rest.dto;

import java.util.Date;
import java.util.List;

public class DailyExpenseDto {
    private Date date;
    private double totalAmount;
    private List<ExpenseDetail> expenseDetails;

    public DailyExpenseDto(Date date, double totalAmount, List<ExpenseDetail> expenseDetails) {
        this.date = date;
        this.totalAmount = totalAmount;
        this.expenseDetails = expenseDetails;
    }

    public static class ExpenseDetail {
        private String title;
        private double amount;
        private String categoryName;

        public ExpenseDetail(String title, double amount, String categoryName) {
            this.title = title;
            this.amount = amount;
            this.categoryName = categoryName;
        }

        // Getters and Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<ExpenseDetail> getExpenseDetails() {
        return expenseDetails;
    }

    public void setExpenseDetails(List<ExpenseDetail> expenseDetails) {
        this.expenseDetails = expenseDetails;
    }
}
