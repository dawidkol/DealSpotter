package pl.dk.dealspotter.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ChangePasswordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin@admin.pl", roles = "ADMIN")
    void itShouldChangeUserPassword() throws Exception {

        // Step 1: Perform an HTTP GET request to /user/change-password to get the change password form
        mockMvc.perform(MockMvcRequestBuilders.get("/user/change-password"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("change-password-form"));

        // Step 2: Perform an HTTP POST request to /user/change-password to change user password
        mockMvc.perform(MockMvcRequestBuilders.post("/user/change-password")
                        .with(csrf())
                        .param("newPassword", "newhardpassword"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/logout"));

    }
}