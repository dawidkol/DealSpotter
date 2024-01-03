package pl.dk.dealspotter.promo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import pl.dk.dealspotter.category.Category;
import pl.dk.dealspotter.category.CategoryRepository;
import pl.dk.dealspotter.promo.dto.PromoDto;
import pl.dk.dealspotter.promo.dto.SavePromoDto;
import pl.dk.dealspotter.user.User;
import pl.dk.dealspotter.user.UserDtoMapper;
import pl.dk.dealspotter.user.UserRepository;
import pl.dk.dealspotter.user.dto.UserDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("test")
class PromoDtoMapperTest {

    private PromoDtoMapper underTest;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDtoMapper userDtoMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new PromoDtoMapper(categoryRepository, userRepository, userDtoMapper);
    }

    @Test
    void itShouldMapToPromoDto() {
        // Given
        Category category = Category.builder()
                .id(1L)
                .name("Electronics")
                .build();

        User user = User.builder()
                .id(1L)
                .build();

        Promo promo = Promo.builder()
                .id(1L)
                .name("Smartphone")
                .description("test description")
                .price(new BigDecimal("488"))
                .urlAddress("http://example.com/promo")
                .category(category)
                .imageFilename("smartphone_promo.jpg")
                .user(user)
                .added(LocalDateTime.now())
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userDtoMapper.map(user)).thenReturn(mock(UserDto.class));

        // When
        PromoDto map = underTest.map(promo);

        // Then
        assertThat(map).isInstanceOfAny(PromoDto.class);
    }

    @Test
    @WithMockUser(username = "admin@admin.pl", roles = "ADMIN")
    void itShouldMapToPromo() {
        // Given
        Category category = Category.builder()
                .id(1L)
                .name("Electronics")
                .build();

        User user = User.builder()
                .firstName("Janusz")
                .password("password")
                .email("admin@admin.pl")
                .build();

        SavePromoDto savePromoDto = SavePromoDto.builder()
                .id(1L)
                .name("Smartphone")
                .description("test description")
                .price(new BigDecimal("488"))
                .urlAddress("http://example.com/promo")
                .category(category.getName())
                .imageFilename("smartphone_promo.jpg")
                .build();

        when(categoryRepository.findByNameIgnoreCase(category.getName())).thenReturn(Optional.of(category));
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // When
        Promo map = underTest.map(savePromoDto);

        // Then
        assertAll(
                () -> assertThat(map.getId()).isEqualTo(savePromoDto.getId()),
                () -> assertThat(map.getName()).isEqualTo(savePromoDto.getName()),
                () -> assertThat(map.getDescription()).isEqualTo(savePromoDto.getDescription()),
                () -> assertThat(map.getUrlAddress()).isEqualTo(savePromoDto.getUrlAddress()),
                () -> assertThat(map.getImageFilename()).isEqualTo(savePromoDto.getImageFilename())
        );
    }

    @Test
    @WithMockUser(username = "admin@admin.pl", roles = "ADMIN")
    void itShouldMapToPromoWithImageSetAsDefaultImage() {
        // Given
        Category category = Category.builder()
                .id(1L)
                .name("Electronics")
                .build();

        User user = User.builder()
                .firstName("Janusz")
                .password("password")
                .email("admin@admin.pl")
                .build();

        SavePromoDto savePromoDto = SavePromoDto.builder()
                .id(1L)
                .name("Smartphone")
                .description("test description")
                .price(new BigDecimal("488"))
                .urlAddress("http://example.com/promo")
                .category(category.getName())
                .imageFilename("")
                .build();

        when(categoryRepository.findByNameIgnoreCase(category.getName())).thenReturn(Optional.of(category));
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // When
        Promo map = underTest.map(savePromoDto);

        // Then
        assertAll(
                () -> assertThat(map.getId()).isEqualTo(savePromoDto.getId()),
                () -> assertThat(map.getName()).isEqualTo(savePromoDto.getName()),
                () -> assertThat(map.getDescription()).isEqualTo(savePromoDto.getDescription()),
                () -> assertThat(map.getUrlAddress()).isEqualTo(savePromoDto.getUrlAddress()),
                () -> assertThat(map.getImageFilename()).isEqualTo("empty-image.png")
        );
    }
}