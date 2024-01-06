package pl.dk.dealspotter.promo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PromoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin@admin.pl", roles = "ADMIN")
    void itShouldSaveNewPromoAndThenUpdate() throws Exception {

        // Step 1: Perform an HTTP GET request to /promo/save to get to save promo form
        Promo promo = new Promo();
        mockMvc.perform(MockMvcRequestBuilders.get("/promo/save").flashAttr("promo", promo))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("save-promo"));


        // Step 2: Perform an HTTP POST request to /promo/save to save new promo with invalid description data(to short)
        MockMultipartFile file = new MockMultipartFile("imageFilename", "empty-image.png", "image/tiff", "file content".getBytes());

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("name", "test promo");
        formData.add("description", "test"); // invalid parameter
        formData.add("price", "99999");
        formData.add("urlAddress", " https://www.test.com.pl");
        formData.add("category", "Elektronika");

        mockMvc.perform(MockMvcRequestBuilders.multipart("/promo/save").file(file)
                        .with(csrf())
                        .params(formData))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("save-promo"));


        // Step 3: Perform an HTTP POST request to /promo/save to save new promo with correct data
        formData.set("description", "test description"); // setting valid parameter

        mockMvc.perform(MockMvcRequestBuilders.multipart("/promo/save").file(file)
                        .with(csrf())
                        .params(formData))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/promo/save/confirmation"));


        // Step 4: Perform an HTTP GET request to /promo/edit/7 to start edit promo
        mockMvc.perform(MockMvcRequestBuilders.get("/promo/edit/7")
                .param("id", "7")
        ).andExpect(view().name("edit-promo"));


        // Step 5: Perform an HTTP POST request to /promo/update to update promo with invalid name data(to short)
        formData.set("name", "a"); // setting invalid parameter

        mockMvc.perform(MockMvcRequestBuilders.multipart("/promo/update").file(file)
                .with(csrf())
                .params(formData))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("edit-promo"));

        // Step 6: Perform an HTTP POST request to /promo/update to update promo with correct data
        formData.add("name", "updated test promo"); // setting valid data

        mockMvc.perform(MockMvcRequestBuilders.multipart("/promo/update").file(file)
                        .with(csrf())
                        .params(formData))
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/promo/edit/confirmation"));
    }
}