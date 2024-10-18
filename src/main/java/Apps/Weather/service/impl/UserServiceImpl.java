package Apps.Weather.service.impl;

import Apps.Weather.customExceptions.InvalidPasswordException;
import Apps.Weather.customExceptions.UserNotFoundException;
import Apps.Weather.dto.UserDto;
import Apps.Weather.models.User;
import Apps.Weather.repository.UserRepository;
import Apps.Weather.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> mapToUserDto(user)).collect(Collectors.toList());
    }

    @Override
    public User saveUser(User user) {
        User saveUser = new User();
        saveUser.setLogin(user.getLogin());
        saveUser.setPassword(hashPassword(user.getPassword()));
        return userRepository.save(saveUser);
    }

    @Override
    public User findBylogin(String login) throws UserNotFoundException {
        User bylogin = userRepository.findBylogin(login);

        if (bylogin == null) {
            throw new UserNotFoundException();
        }

        return bylogin;
    }

    @Override
    public boolean isLoginUnique(User user) {
        user = userRepository.findBylogin(user.getLogin());
        return user == null;
    }

    @Override
    public boolean checkPassword(String firstPassword, String secondPassword) throws InvalidPasswordException {
        boolean matches = new BCryptPasswordEncoder().matches(firstPassword, secondPassword);
        if (matches) {
            return true;
        }else {
            throw new InvalidPasswordException();
        }
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .build();
        return userDto;
    }

    private String hashPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }


}
