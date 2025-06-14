package ba.edu.ibu.budgettracker.rest.controller;

import ba.edu.ibu.budgettracker.core.service.UserService;
import ba.edu.ibu.budgettracker.rest.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void testGetLoginUser_Success() throws Exception {
        // Given
        String email = "test@example.com";
        UserDto userDto = new UserDto(1L, email);

        when(userService.findByEmail(email)).thenReturn(Optional.of(userDto));

        // When & Then
        mockMvc.perform(get("/api/users/login")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    void testGetLoginUser_NotFound() throws Exception {
        // Given
        String email = "notfound@example.com";

        when(userService.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/users/login")
                        .param("email", email))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPostLoginUser_Success() throws Exception {
        // Given
        String email = "newuser@example.com";
        UserDto userDto = new UserDto(1L, email);

        when(userService.createUser(email)).thenReturn(userDto);

        // When & Then
        mockMvc.perform(post("/api/users/login")
                        .content(email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }
}
