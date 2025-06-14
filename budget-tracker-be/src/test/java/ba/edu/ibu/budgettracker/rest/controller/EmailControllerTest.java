package ba.edu.ibu.budgettracker.rest.controller;

import ba.edu.ibu.budgettracker.core.service.EmailService;
import ba.edu.ibu.budgettracker.rest.dto.EmailRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EmailControllerTest {

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailController emailController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(emailController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testSendUserReportEmail_Success() throws Exception {
        // Given
        EmailRequest request = new EmailRequest("user@example.com", 1L, 1, 2024);

        // When & Then
        mockMvc.perform(post("/api/emails/send-report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Email sent successfully to: user@example.com"));

        // Verify that the service was called with the correct parameters
        verify(emailService).sendUserReportEmail("user@example.com", 1L, 1, 2024);
    }

    @Test
    void testSendUserReportEmail_InvalidEmail() throws Exception {
        // Given
        EmailRequest request = new EmailRequest("invalid-email", 1L, 1, 2024);

        doThrow(new IllegalArgumentException("Invalid email address"))
                .when(emailService)
                .sendUserReportEmail("invalid-email", 1L, 1, 2024);

        // When & Then
        mockMvc.perform(post("/api/emails/send-report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Invalid email address"));
    }

    @Test
    void testSendUserReportEmail_InternalError() throws Exception {
        // Given
        EmailRequest request = new EmailRequest("user@example.com", 1L, 1, 2024);

        doThrow(new RuntimeException("Unexpected error"))
                .when(emailService)
                .sendUserReportEmail("user@example.com", 1L, 1, 2024);

        // When & Then
        mockMvc.perform(post("/api/emails/send-report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An unexpected error occurred."));
    }
}
