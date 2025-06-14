package ba.edu.ibu.budgettracker.api.impl.sendgridclient;

import ba.edu.ibu.budgettracker.core.api.EmailClient;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SendGridClient implements EmailClient {

    private final String apiKey;
    private final String fromEmail;

    public SendGridClient(@Value("${sendgrid.api-key}") String apiKey, @Value("${sendgrid.from-email}") String fromEmail) {
        this.apiKey = apiKey;
        this.fromEmail = fromEmail;
    }

    @Override
    public void sendEmail(String to, String body) {
        String subject = "Budget Tracker";

        Email from = new Email(fromEmail);
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            if (response.getStatusCode() >= 400) {
                throw new IOException("Failed to send email. Status Code: " + response.getStatusCode() + ", Response: " + response.getBody());
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error communicating with SendGrid: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean validateEmail(String to) {
        return to != null && to.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    }
}
