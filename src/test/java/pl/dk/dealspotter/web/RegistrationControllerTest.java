package pl.dk.dealspotter.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dk.dealspotter.user.dto.UserRegistrationDto;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldRegisterUser() throws Exception {

        // Step 1: Perform an HTTP GET request to /register to get the registration form
        UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder().build();
        mockMvc.perform(MockMvcRequestBuilders.get("/register").flashAttr("user", userRegistrationDto))
                .andExpect(MockMvcResultMatchers.view().name("register"));

        // Step 2: Perform an HTTP POST request to /register with invalid email
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .with(csrf())
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "invalidemail") // invalid email format
                        .param("password", "password123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("register"));


        // Step 3: Perform an HTTP POST request to /register with valid data
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .with(csrf())
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john.doe@example.com")
                        .param("password", "password123"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/confirmation"));

        // Step 4: Perform an HTTP GET request to /confirmation to check if it redirects to the correct view
        mockMvc.perform(MockMvcRequestBuilders.get("/confirmation")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("registration-confirmation"));
    }

}