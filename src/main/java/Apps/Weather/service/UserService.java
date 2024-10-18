package Apps.Weather.service;

import Apps.Weather.customExceptions.InvalidPasswordException;
import Apps.Weather.customExceptions.UserNotFoundException;
import Apps.Weather.dto.UserDto;
import Apps.Weather.models.User;

import java.util.List;

public interface UserService {
    List<UserDto> findAllUsers();

    User saveUser(User user);

    User findBylogin(String login) throws UserNotFoundException;

    boolean isLoginUnique(User user);

    boolean checkPassword(String firstPassword, String secondPassword) throws InvalidPasswordException;
}
