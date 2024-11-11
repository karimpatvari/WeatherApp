package Apps.Weather.service.impl;

import Apps.Weather.customExceptions.InvalidCredentialsException;
import Apps.Weather.customExceptions.UserAlreadyExistsException;
import Apps.Weather.dto.UserDto;
import Apps.Weather.models.User;
import Apps.Weather.repository.UserRepository;
import Apps.Weather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findBylogin(String login){
        return userRepository.findBylogin(login);
    }

    @Override
    public User validateAndSaveUser(User user) throws UserAlreadyExistsException {
        validateUserDoesNotExist(user);
        return saveUserWithHashedPassword(user);
    }

    private void validateUserDoesNotExist(User user) throws UserAlreadyExistsException {
        Optional<User> existingUser = findBylogin(user.getLogin());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("A user with this login already exists.");
        }
    }

    private User saveUserWithHashedPassword(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }

    private UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .build();
    }


}
