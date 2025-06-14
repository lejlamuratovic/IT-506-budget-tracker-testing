package ba.edu.ibu.budgettracker.rest.dto;

public class UserRequest {
    private String email;

    public UserRequest() {}

    public UserRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
