package ba.edu.ibu.budgettracker.core.service;

import ba.edu.ibu.budgettracker.core.model.User;
import ba.edu.ibu.budgettracker.core.repository.UserRepository;
import ba.edu.ibu.budgettracker.rest.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

    public Optional<UserDto> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> new UserDto(user.getId(), user.getEmail()));
    }

    public UserDto createUser(String email) {
        User newUser = new User(email);
        userRepository.save(newUser);
        return new UserDto(newUser.getId(), newUser.getEmail());
    }
}
