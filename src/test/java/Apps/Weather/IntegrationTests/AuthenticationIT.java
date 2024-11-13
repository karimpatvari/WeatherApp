package Apps.Weather.IntegrationTests;

import Apps.Weather.customExceptions.UserAlreadyExistsException;
import Apps.Weather.models.Session;
import Apps.Weather.models.User;
import Apps.Weather.repository.SessionRepository;
import Apps.Weather.repository.UserRepository;
import Apps.Weather.service.SessionService;
import Apps.Weather.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        sessionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void Authentication_Success() throws Exception {

        User user = userService.validateAndSaveUser(new User("login", "password"));

        // Now perform the registration request
        mockMvc.perform(post("/login")
                        .param("login", "login")
                        .param("password", "password"))
                .andExpect(status().isFound())  // Check for a 302 status
                .andExpect(redirectedUrl("/dashboard"));

        assertTrue(sessionService.findByUser(user).isPresent());
    }

    @Test
    void Authentication_UserNotExists() throws Exception {

        mockMvc.perform(post("/login")
                        .param("login", "login")
                        .param("password", "password"))
                .andExpect(view().name("login-page"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", "User with login login not found"));

    }

    @Test
    void Authentication_InvalidCredentials() throws Exception {

        userService.validateAndSaveUser(new User("login", "password"));

        mockMvc.perform(post("/login")
                        .param("login", "login")
                        .param("password", "wrongPassword"))
                .andExpect(view().name("login-page"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", "Wrong password"));

    }


}
