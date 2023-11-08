package pl.dk.dealspotter.promo;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.dk.dealspotter.file.FileStorageRepository;
import pl.dk.dealspotter.promo.dto.SavePromoDto;

import java.math.BigDecimal;

@WebMvcTest(PromoController.class)
@AutoConfigureMockMvc(addFilters = false)
class PromoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PromoService promoService;

    @MockBean
    FileStorageRepository fileStorageRepository;

    SavePromoDto promo;

    @BeforeEach
    void setUp() {
        promo = SavePromoDto.builder()
                .id(1L)
                .name("Nowy MacBook Air 15")
                .description("Najszybszy procesor od Applea, 15 calowy ekran, czas pracy baterii do 15h")
                .price(new BigDecimal("7999.00"))
                .urlAddress("https://www.mediaexpert.pl/komputery-i-tablety/laptopy-i-ultrabooki/laptopy/" +
                        "notebook-apple-macbook-air-2023-m2-8x-8gb-512ssd-m2-10x-15-3-macos-s-g?gclid" +
                        "=Cj0KCQjwjt-oBhDKARIsABVRB0wSqYDfdNSN1Gyi6EVC-oONHrEXHn02GeqhNat6ocYjcyaQVT9X3mAaAvCbEALw_wcB")
                .category("Elektronika")
                .imageFilename(null)
                .build();
    }

    @Test
    void shouldFindAllPromos() throws Exception {

//        mockMvc.perform(post("/promo/save"))..andExpect(status().isCreated());
    }
}
