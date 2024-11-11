package Apps.Weather.IntegrationTests;

import Apps.Weather.customExceptions.UserAlreadyExistsException;
import Apps.Weather.models.User;
import Apps.Weather.service.UserService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RegistrationIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    public void testSuccessfulRegistration() throws Exception {

        mockMvc.perform(post("/register")
                        .param("login", "newUser")
                        .param("password", "newPassword"))
                .andExpect(redirectedUrl("/login"));

        Optional<User> user = userService.findBylogin("newUser");
        assertTrue(user.isPresent());
        assertEquals("newUser", user.get().getLogin());
    }

    @Test
    public void testNotUniqueLoginThrowsException() throws Exception {

        userService.validateAndSaveUser(new User("existingUser", "newPassword"));

        // Now perform the registration request with the same login
        mockMvc.perform(post("/register")
                        .param("login", "existingUser")
                        .param("password", "newPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration-page"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", "A user with this login already exists."));
    }

}
