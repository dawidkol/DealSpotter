package pl.dk.dealspotter.promo;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import pl.dk.dealspotter.user.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@AutoConfigureMockMvc(addFilters = false)
@Log4j2
class PromoServiceTest {

    @Autowired
    private PromoService promoService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;


    @Test
    void shouldFindSixPromos() {
        //given when
        List<PromoDto> allPromo = promoService.findAllPromo();

        int currentSize = allPromo.size();
        int expectedSize = 6;

        //then
        assertThat(currentSize).isEqualTo(expectedSize);
    }

    @Test
    void shouldFindPromosByGivenCategoryName() {
        //given
        String categoryName = "Elektronika";

        //when
        List<String> categoryList = promoService.findByCategory(categoryName).stream().map(PromoDto::getCategory).toList();

        //then
        assertAll(
                () -> assertThat(categoryList.stream()).allMatch(category -> category.equalsIgnoreCase(categoryName)),
                () -> assertThat(categoryList.stream()).hasSize(3)
        );
    }

    @Test
    void shouldFindByGivenNameAndCategory() {
        //given
        String categoryName = "Elektronika";
        String name = "Mac";

        //when
        List<PromoDto> byNameAndCategory = promoService.findByNameAndCategory(name, categoryName);
        List<String> categoryList = byNameAndCategory.stream().map(PromoDto::getCategory).toList();
        List<String> nameList = byNameAndCategory.stream().map(PromoDto::getName).toList();

        //then
        assertAll(
                () -> categoryList.stream().allMatch(category -> category.equalsIgnoreCase(categoryName)),
                () -> nameList.stream().allMatch(n -> n.equalsIgnoreCase(name))
        );
    }

    @Test
    @WithMockUser(username = "mateusz.kowalski@abc.pl", password = "simplePass")
    void shouldFoundThreePromos() {
        //given
        String firstUserPromoName = "Apple MacBook Air M2";
        String secondUserPromoName = "Czapka męska z daszkiem 4F Bejsbolówka 2023 L";
        String thirdUserPromoName = "Tabliczka Tablica ADRESOWA ALUMINIOWA - Numer domu";

        //when
        List<PromoDto> promosByUsername = promoService.findPromosByUsername();

        //then
        assertAll(
                () -> assertThat(promosByUsername.size()).isEqualTo(3),
                () -> assertThat(promosByUsername.get(0).getName()).isEqualToIgnoringCase(firstUserPromoName),
                () -> assertThat(promosByUsername.get(1).getName()).isEqualToIgnoringCase(secondUserPromoName),
                () -> assertThat(promosByUsername.get(2).getName()).isEqualToIgnoringCase(thirdUserPromoName)
        );
    }

    @Test
    void shouldFindPromoById() {
        //given
        Long id = 1L;

        //when
        Optional<PromoDto> promoById = promoService.getPromoById(id);

        //then
        assertAll(
                () -> assertThat(promoById).isPresent(),
                () -> assertThat(promoById).isNotEmpty()
        );
    }

    @Test
    @WithMockUser(username = "mateusz.kowalski@abc.pl", password = "simplePass")
    void shouldSavePromoWithId7() {
        //given
        SavePromoDto promoToSave = SavePromoDto.builder()
                .name("Promocja 1")
                .description("Najlepsza promocja wszechczasów. Unikatowy, niezawodny rolex")
                .price(new BigDecimal("200"))
                .urlAddress("https://www.najlepszapromocja.pl")
                .category("Elektronika")
                .imageFilename("imageFilename")
                .build();

        //when
        promoService.savePromo(promoToSave);
        Optional<PromoDto> promoById = promoService.getPromoById(7L);

        //then
        assertAll(
                () -> assertThat(promoById).isNotEmpty(),
                () -> assertThat(promoById).isPresent(),
                () -> assertThat(promoById.orElseThrow().getId()).isEqualTo(7),
                () -> assertThat(promoById.orElseThrow().getName()).isEqualToIgnoringCase(promoToSave.getName()),
                () -> assertThat(promoById.orElseThrow().getDescription()).isEqualToIgnoringCase(promoToSave.getDescription()),
                () -> assertThat(promoById.orElseThrow().getPrice()).isEqualByComparingTo(promoToSave.getPrice()),
                () -> assertThat(promoById.orElseThrow().getUrlAddress()).isEqualTo(promoToSave.getUrlAddress()),
                () -> assertThat(promoById.orElseThrow().getImageFilename()).isEqualToIgnoringCase(promoToSave.getImageFilename())
        );
    }

    @Test
    @WithMockUser(username = "mateusz.kowalski@abc.pl", password = "simplePass")
    void shouldUpdatePromo() {
        //given
        PromoDto promoDto = promoService.getPromoById(2L).orElseThrow();
        String newPromoName = "Najlepszy MacBook na świecie";
        promoDto.setName(newPromoName);
        SavePromoDto promoToUpdate = SavePromoDto.builder()
                .id(promoDto.getId())
                .name(promoDto.getName())
                .description(promoDto.getDescription())
                .price(promoDto.getPrice())
                .urlAddress(promoDto.getUrlAddress())
                .category(promoDto.getCategory())
                .imageFilename("empty-image.png")
                .build();

        log.info(promoDto.getImageFilename());

        //when
        promoService.updatePromo(promoToUpdate);
        PromoDto updatedPromo = promoService.getPromoById(2l).orElseThrow();

        //then
        assertAll(
                () -> assertTrue(updatedPromo.getName().equalsIgnoreCase(newPromoName))
        );


    }
}