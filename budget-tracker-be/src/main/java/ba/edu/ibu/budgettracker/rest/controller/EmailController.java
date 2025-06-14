package ba.edu.ibu.budgettracker.rest.controller;

import ba.edu.ibu.budgettracker.core.service.EmailService;
import ba.edu.ibu.budgettracker.rest.dto.EmailRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-report")
    public ResponseEntity<String> sendUserReportEmail(@RequestBody EmailRequest request) {
        try {
            emailService.sendUserReportEmail(request.getEmail(), request.getUserId(), request.getMonth(), request.getYear());
            return ResponseEntity.ok("Email sent successfully to: " + request.getEmail());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}
