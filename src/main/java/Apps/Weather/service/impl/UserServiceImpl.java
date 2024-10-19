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

import java.util.List;
import java.util.Optional;
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
    public User saveUser(User user) throws UserAlreadyExistsException{
        Optional<User> existingUser = findBylogin(user.getLogin());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("A user with this login already exists.");
        }

        user.setPassword(hashPassword(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findBylogin(String login){
        return userRepository.findBylogin(login);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean checkPassword(String rawPassword, String encodedPassword){
        return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
    }

    private UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .build();
    }

    private String hashPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }


}
