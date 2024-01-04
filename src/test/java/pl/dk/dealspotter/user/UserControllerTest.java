package pl.dk.dealspotter.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin@admin.pl", roles = "ADMIN")
    void itShouldPerformUserAndPromoActionsSuccessfully() throws Exception {

        // Step 1: Perform HTTP GET request to /user to get to user template
        mockMvc.perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user"));

        // Step 2: Perform HTTP GET request to /user/promo/all to see all users promo
        mockMvc.perform(MockMvcRequestBuilders.get("/user/promo/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("promo-list"));

        // Step 3: Perform HTTP GET request to /user/promo/delete/1 to delete one promo
        mockMvc.perform(MockMvcRequestBuilders.get("/user/promo/delete/1"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/promo/all"));

        // Step 4: Perform HTTP GET request to /user/all-users to see all users
        mockMvc.perform(MockMvcRequestBuilders.get("/user/all-users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("all-users"));

        // Step 5: Perform HTTP GET request to /user/delete/mateusz.kowalski@abc.pl to delete user
        mockMvc.perform(MockMvcRequestBuilders.get("/user/delete/mateusz.kowalski@abc.pl"))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/all-users"));

    }
}