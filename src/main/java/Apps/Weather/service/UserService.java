package Apps.Weather.service;

import Apps.Weather.customExceptions.InvalidCredentialsException;
import Apps.Weather.customExceptions.UserAlreadyExistsException;
import Apps.Weather.dto.UserDto;
import Apps.Weather.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDto> findAllUsers();

    User saveUser(User user) throws UserAlreadyExistsException;

    Optional<User> findBylogin(String login);

    Optional<User> findById(Integer id);

    boolean checkPassword(String rawPassword, String encodedPassword);
}
