package Apps.Weather.service;

import Apps.Weather.dto.UserDto;
import Apps.Weather.models.User;

import java.util.List;

public interface UserService {
    List<UserDto> findAllUsers();
}
