package ba.edu.ibu.budgettracker.core.service;

import ba.edu.ibu.budgettracker.core.model.User;
import ba.edu.ibu.budgettracker.core.repository.UserRepository;
import ba.edu.ibu.budgettracker.rest.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserEntityById_Success() {
        // Given
        Long userId = 1L;
        User user = new User("test@example.com");
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        User result = userService.getUserEntityById(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getEmail()).isEqualTo("test@example.com");

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetUserEntityById_UserNotFound() {
        // Given
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.getUserEntityById(userId));
        assertThat(exception.getMessage()).isEqualTo("User not found with id: " + userId);

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testFindByEmail_Success() {
        // Given
        String email = "test@example.com";
        User user = new User(email);
        user.setId(1L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        Optional<UserDto> result = userService.findByEmail(email);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getEmail()).isEqualTo(email);

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindByEmail_UserNotFound() {
        // Given
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        Optional<UserDto> result = userService.findByEmail(email);

        // Then
        assertThat(result).isNotPresent();

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testCreateUser_Success() {
        // Given
        String email = "test@example.com";
        User user = new User(email);
        user.setId(1L);

        // Mock the save method to return the same user instance
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        // When
        UserDto result = userService.createUser(email);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo(email);

        verify(userRepository, times(1)).save(any(User.class));
    }
}
