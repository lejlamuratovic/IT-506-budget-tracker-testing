package ba.edu.ibu.budgettracker.rest.dto;

public class EmailRequest {
    private String email;
    private Long userId;
    private int month;
    private int year;

    public EmailRequest() {
    }

    public EmailRequest(String email, Long userId, int month, int year) {
        this.email = email;
        this.userId = userId;
        this.month = month;
        this.year = year;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
