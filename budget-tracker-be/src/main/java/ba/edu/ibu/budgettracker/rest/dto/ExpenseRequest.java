package ba.edu.ibu.budgettracker.rest.dto;

import java.util.Date;

public class ExpenseRequest {
    private String title;
    private Double amount;
    private Date date;
    private Long categoryId;
    private Long userId;

    public ExpenseRequest() {}

    public ExpenseRequest(String title, Double amount, Date date, Long categoryId, Long userId) {
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.categoryId = categoryId;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
