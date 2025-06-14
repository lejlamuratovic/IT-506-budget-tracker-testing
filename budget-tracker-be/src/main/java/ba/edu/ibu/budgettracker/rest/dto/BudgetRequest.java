package ba.edu.ibu.budgettracker.rest.dto;

public class BudgetRequest {
    private Double amount;
    private Integer month;
    private Integer year;
    private Long userId;

    public BudgetRequest() {}

    public BudgetRequest(Double amount, Integer month, Integer year, Long userId) {
        this.amount = amount;
        this.month = month;
        this.year = year;
        this.userId = userId;
    }

    // Getters and Setters
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
