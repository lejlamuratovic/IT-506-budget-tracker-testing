package ba.edu.ibu.budgettracker.core.api;

public interface EmailClient {
    void sendEmail(String to, String body);
    boolean validateEmail(String to);
}
