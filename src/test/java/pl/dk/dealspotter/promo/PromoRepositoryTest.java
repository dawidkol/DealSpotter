package pl.dk.dealspotter.promo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pl.dk.dealspotter.category.Category;
import pl.dk.dealspotter.category.CategoryType;
import pl.dk.dealspotter.user.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest(properties = {"spring.liquibase.enabled=false", "spring.jpa.hibernate.ddl-auto=create-drop"})
@ActiveProfiles(value = "test")
class PromoRepositoryTest {

    @Autowired
    private PromoRepository underTest;

    @Test
    void itShouldFindUserPromosByGivenEmail() {
        // Given
        String email = "john@rambo.com";

        User user = User.builder()
                .firstName("John")
                .lastName("Rambo")
                .email(email)
                .password("rambo123")
                .roles(Collections.emptySet())
                .promo(Collections.emptyList())
                .build();

        Promo promo = Promo.builder()
                .user(user)
                .name("testPromo")
                .description("best promo you ever saw")
                .price(new BigDecimal("99.99"))
                .build();

        Promo savedPromo = underTest.save(promo);

        // When
        List<Promo> promosByUserEmail = underTest.findPromosByUser_Email(email);

        // Then
        assertAll(
                () -> assertThat(promosByUserEmail).containsOnly(savedPromo),
                () -> assertThat(promosByUserEmail).hasSize(1)
        );
    }

    @Test
    void itShouldFindAllPromosOrderByAddedDateDescending() {
        // Given
        User user = User.builder()
                .firstName("John")
                .lastName("Rambo")
                .email("john@rambo.com")
                .password("rambo123")
                .roles(Collections.emptySet())
                .promo(Collections.emptyList())
                .build();

        Promo promo1 = Promo.builder()
                .user(user)
                .name("testPromo1")
                .description("best promo you ever saw 1")
                .price(new BigDecimal("99.99"))
                .added(LocalDateTime.now().minusDays(1L))
                .build();

        Promo promo2 = Promo.builder()
                .user(user)
                .name("testPromo2")
                .description("best promo you ever saw 2")
                .price(new BigDecimal("99.99"))
                .added(LocalDateTime.now())
                .build();

        underTest.save(promo1);
        underTest.save(promo2);
        // When

        List<Promo> allPromosOrderByDateDescending = underTest.findAllPromosOrderByDateDescending();

        // Then
        assertAll(
                () -> assertThat(allPromosOrderByDateDescending.size()).isEqualTo(2),
                () -> assertThat(allPromosOrderByDateDescending).contains(promo1),
                () -> assertThat(allPromosOrderByDateDescending).contains(promo2),
                () -> assertThat(allPromosOrderByDateDescending).containsExactly(promo2, promo1)
        );
    }

    @Test
    void itShouldFindAllPromosByGivenCategoryName() {
        // Given
        User user = User.builder()
                .firstName("John")
                .lastName("Rambo")
                .email("john@rambo.com")
                .password("rambo123")
                .roles(Collections.emptySet())
                .promo(Collections.emptyList())
                .build();

        String categoryName = CategoryType.All.getDescription();
        Category category = Category.builder()
                .name(categoryName)
                .promoList(Collections.emptyList())
                .build();

        Promo promo1 = Promo.builder()
                .user(user)
                .name("testPromo1")
                .description("best promo you ever saw 1")
                .price(new BigDecimal("99.99"))
                .added(LocalDateTime.now().minusDays(1L))
                .category(category)
                .build();

        Promo promo2 = Promo.builder()
                .user(user)
                .name("testPromo2")
                .description("best promo you ever saw 2")
                .price(new BigDecimal("99.99"))
                .category(category)
                .added(LocalDateTime.now())
                .build();

        underTest.save(promo1);
        underTest.save(promo2);


        // When
        List<Promo> allByCategoryNameIgnoreCase = underTest.findAllByCategory(categoryName);

        // Then
        assertAll(
                () -> assertThat(allByCategoryNameIgnoreCase).hasSize(2)
        );
    }

    @Test
    void itShouldFindAllByNameByGivenNameAndCategory() {
        // Given
        User user = User.builder()
                .firstName("John")
                .lastName("Rambo")
                .email("john@rambo.com")
                .password("rambo123")
                .roles(Collections.emptySet())
                .promo(Collections.emptyList())
                .build();

        String categoryName = CategoryType.ELECTRONICS.getDescription();
        Category category = Category.builder()
                .name(categoryName)
                .promoList(Collections.emptyList())
                .build();

        Promo promo1 = Promo.builder()
                .user(user)
                .name("testPromo1")
                .description("best promo you ever saw 1")
                .price(new BigDecimal("99.99"))
                .added(LocalDateTime.now().minusDays(1L))
                .category(category)
                .build();

        Promo promo2 = Promo.builder()
                .user(user)
                .name("testPromo2")
                .description("best promo you ever saw 2")
                .price(new BigDecimal("99.99"))
                .category(category)
                .added(LocalDateTime.now())
                .build();

        underTest.save(promo1);
        underTest.save(promo2);

        String subName = promo1.getName().substring(3, 5);

        // When
        List<Promo> allByNameContainingIgnoreCaseAndCategoryNameIgnoreCase =
                underTest.findAllByNameContainingIgnoreCaseAndCategory_NameIgnoreCase(subName, categoryName);

        // Then
        assertAll(
                () -> assertThat(allByNameContainingIgnoreCaseAndCategoryNameIgnoreCase).hasSize(2),
                () -> assertThat(allByNameContainingIgnoreCaseAndCategoryNameIgnoreCase).containsExactly(promo1, promo2)
        );
    }

    @Test
    void itShouldFindAllByName() {
        // Given
        User user = User.builder()
                .firstName("John")
                .lastName("Rambo")
                .email("john@rambo.com")
                .password("rambo123")
                .roles(Collections.emptySet())
                .promo(Collections.emptyList())
                .build();

        String categoryName = CategoryType.ELECTRONICS.getDescription();
        Category category = Category.builder()
                .name(categoryName)
                .promoList(Collections.emptyList())
                .build();

        Promo promo1 = Promo.builder()
                .user(user)
                .name("testPromo1")
                .description("best promo you ever saw 1")
                .price(new BigDecimal("99.99"))
                .added(LocalDateTime.now().minusDays(1L))
                .category(category)
                .build();

        Promo promo2 = Promo.builder()
                .user(user)
                .name("testPromo2")
                .description("best promo you ever saw 2")
                .price(new BigDecimal("99.99"))
                .category(category)
                .added(LocalDateTime.now())
                .build();

        underTest.save(promo1);
        underTest.save(promo2);

        String subName = promo1.getName().substring(1, 5);

        // When
        List<Promo> allByNameContainingIgnoreCase = underTest.findAllByNameContainingIgnoreCase(subName);

        // Then
        assertAll(
                () -> assertThat(allByNameContainingIgnoreCase).hasSize(2),
                () -> assertThat(allByNameContainingIgnoreCase).containsExactly(promo1, promo2)
        );
    }
}