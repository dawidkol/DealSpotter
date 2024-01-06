package pl.dk.dealspotter.category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void xxx() throws Exception {

        // Step 1: Perform an HTTP GET request to /category/Wszystkie kategorie to get all promos
        String allCategory = CategoryType.All.getDescription();
        System.out.println(allCategory);
        mockMvc.perform(MockMvcRequestBuilders.get("/category/%s".formatted(allCategory)))
                .andExpect(MockMvcResultMatchers.view().name("index"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Step 2: Perform an HTTP GET request to /category/Elektronika to get promos in elektronika category
        String electronics = CategoryType.ELECTRONICS.getDescription();
        System.out.println(allCategory);
        mockMvc.perform(MockMvcRequestBuilders.get("/category/%s".formatted(electronics)))
                .andExpect(MockMvcResultMatchers.view().name("index"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}