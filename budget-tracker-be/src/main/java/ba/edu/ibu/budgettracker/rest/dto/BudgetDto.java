package ba.edu.ibu.budgettracker.rest.dto;

public class BudgetDto {
    private Long id;
    private Double amount;
    private Double remaining;
    private Integer month;
    private Integer year;
    private Long userId;

    public BudgetDto() {}

    public BudgetDto(Long id, Double amount, Double remaining, Integer month, Integer year, Long userId) {
        this.id = id;
        this.amount = amount;
        this.remaining = remaining;
        this.month = month;
        this.year = year;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getRemaining() {
        return remaining;
    }

    public void setRemaining(Double remaining) {
        this.remaining = remaining;
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
