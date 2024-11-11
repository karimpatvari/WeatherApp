package Apps.Weather.service;

import Apps.Weather.customExceptions.InvalidCredentialsException;
import Apps.Weather.customExceptions.UserAlreadyExistsException;
import Apps.Weather.dto.UserDto;
import Apps.Weather.models.User;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User validateAndSaveUser(User user) throws UserAlreadyExistsException;

    Optional<User> findBylogin(String login);

}
