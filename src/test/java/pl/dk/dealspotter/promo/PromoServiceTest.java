package pl.dk.dealspotter.promo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import pl.dk.dealspotter.category.Category;
import pl.dk.dealspotter.category.CategoryType;
import pl.dk.dealspotter.promo.dto.PromoDto;
import pl.dk.dealspotter.promo.dto.SavePromoDto;
import pl.dk.dealspotter.user.User;
import pl.dk.dealspotter.user.UserRole;
import pl.dk.dealspotter.user.UserService;
import pl.dk.dealspotter.user.dto.UserCredentialsDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PromoServiceTest {
    @Mock
    private PromoRepository promoRepository;
    @Mock
    private UserService userService;
    @Mock
    private PromoDtoMapper promoDtoMapper;
    private PromoService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new PromoService(promoRepository, promoDtoMapper, userService);
    }

    @Test
    void itShouldFindAllPromos() {
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

        List<Promo> returnList = new ArrayList<>();
        returnList.add(promo1);
        returnList.add(promo2);

        when(promoRepository.findAllPromosOrderByDateDescending()).thenReturn(returnList);

        // When
        List<PromoDto> allPromo = underTest.findAllPromo();

        // Then
        assertAll(
                () -> assertThat(allPromo).hasSize(2)
        );
    }

    @Test
    void itShouldFindPromoByGivenCategoryName() {
        // Given
        User user = User.builder()
                .firstName("John")
                .lastName("Rambo")
                .email("john@rambo.com")
                .password("rambo123")
                .roles(Collections.emptySet())
                .promo(Collections.emptyList())
                .build();
        String categoryName = "Elektronika";

        Category category = Category.builder()
                .id(1L)
                .name(categoryName)
                .promoList(Collections.emptyList())
                .build();

        Promo promo = Promo.builder()
                .user(user)
                .name("testPromo1")
                .description("best promo you ever saw 1")
                .price(new BigDecimal("99.99"))
                .added(LocalDateTime.now().minusDays(1L))
                .category(category)
                .build();

        when(promoRepository.findAllByCategory_NameIgnoreCase(categoryName)).thenReturn(List.of(promo));

        // When
        List<PromoDto> listPromoDto = underTest.findByCategory(categoryName);

        // Then
        assertAll(
                () -> assertThat(listPromoDto).hasSize(1)
        );
    }

    @Test
    void itShouldFindAllPromoByGivenNameAndCategory() {
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
        System.out.println(categoryName);

        Category category = Category.builder()
                .id(1L)
                .name(categoryName)
                .promoList(Collections.emptyList())
                .build();

        Promo promo = Promo.builder()
                .user(user)
                .name("testPromo1")
                .description("best promo you ever saw 1")
                .price(new BigDecimal("99.99"))
                .added(LocalDateTime.now().minusDays(1L))
                .category(category)
                .build();

        String partOfTheName = promo.getName().substring(2, 6);

        when(promoRepository.findAllByNameContainingIgnoreCaseAndCategory_NameIgnoreCase(partOfTheName, categoryName))
                .thenReturn(List.of(promo));

        // When
        List<PromoDto> promoListByNameAndCategory = underTest.findByNameAndCategory(partOfTheName, categoryName);

        // Then
        assertAll(
                () -> assertThat(promoListByNameAndCategory).isNotNull(),
                () -> assertThat(promoListByNameAndCategory).isNotEmpty()
        );
    }

    @Test
    void itShouldFindAllPromoByGivenNameAndCategoryNameEqualsToAll() {
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
                .id(1L)
                .name(categoryName)
                .promoList(Collections.emptyList())
                .build();

        Promo promo = Promo.builder()
                .user(user)
                .name("testPromo1")
                .description("best promo you ever saw 1")
                .price(new BigDecimal("99.99"))
                .added(LocalDateTime.now().minusDays(1L))
                .category(category)
                .build();

        String partOfTheName = promo.getName().substring(2, 6);

        when(promoRepository.findAllByNameContainingIgnoreCase(partOfTheName))
                .thenReturn(List.of(promo));

        // When
        List<PromoDto> promoListByNameAndCategory = underTest.findByNameAndCategory(partOfTheName, categoryName);

        // Then
        assertAll(
                () -> assertThat(promoListByNameAndCategory).isNotNull(),
                () -> assertThat(promoListByNameAndCategory).isNotEmpty()
        );
    }

    @Test
    void itShouldFindCurrentUsernamePromos() {
        // Given
        String email = "john@rambo.com";
        UserRole userRole = UserRole.builder()
                .id(1L)
                .name("USER")
                .description("Can log in service")
                .build();

        User user = User.builder()
                .firstName("John")
                .lastName("Rambo")
                .email(email)
                .password("currentPassword")
                .roles(Set.of(userRole))
                .build();

        Promo promo = Promo.builder()
                .user(user)
                .name("testPromo1")
                .description("best promo you ever saw 1")
                .price(new BigDecimal("99.99"))
                .added(LocalDateTime.now().minusDays(1L))
                .category(new Category())
                .build();

        when(promoRepository.findPromosByUser_Email(email)).thenReturn(List.of(promo));

        // When
        List<PromoDto> promosByUsername = underTest.findPromosByUsername(email);

        // Then
        assertAll(
                () -> assertThat(promosByUsername).hasSize(1)
        );
    }

    @Test
    void itShouldSavePromoToDb() {
        // Given
        String email = "john@rambo.com";

        SavePromoDto savePromoDto = SavePromoDto.builder()
                .name("testPromo1")
                .description("best promo you ever saw 1")
                .price(new BigDecimal("99.99"))
                .category(CategoryType.ELECTRONICS.getDescription())
                .build();

        UserCredentialsDto userCredentialsDto = UserCredentialsDto.builder()
                .email(email)
                .password("currentPassword")
                .roles(Set.of("ADMIN"))
                .build();

        when(userService.findCredentialsByEmail(email)).thenReturn(Optional.of(userCredentialsDto));

        // When
        underTest.savePromo(savePromoDto, email);

        // Then
        assertAll(
                () -> verify(promoRepository, times(1)).save(any())
        );
    }

    @Test
    void itShouldThrow403Exception() {
        // Given
        String email = "john@rambo.com";
        SavePromoDto savePromoDto = SavePromoDto.builder()
                .name("testPromo1")
                .description("best promo you ever saw 1")
                .price(new BigDecimal("99.99"))
                .category(CategoryType.ELECTRONICS.getDescription())
                .build();

        UserCredentialsDto userCredentialsDto = UserCredentialsDto.builder()
                .email(email)
                .password("currentPassword")
                .roles(Set.of("NON_ADMIN_NON_USER"))
                .build();
        when(userService.findCredentialsByEmail(email)).thenReturn(Optional.of(userCredentialsDto));

        // When
        // Then
        assertAll(
                () -> assertThrows(ResponseStatusException.class, () -> underTest.savePromo(savePromoDto, email))
        );
    }

    @Test
    void itShouldUpdatePromoWhenUserIsAmin() {
        // Given
        String email = "john@rambo.com";

        SavePromoDto savePromoDto = SavePromoDto.builder()
                .name("testPromo1")
                .description("best promo you ever saw 1")
                .price(new BigDecimal("99.99"))
                .category(CategoryType.ELECTRONICS.getDescription())
                .build();

        UserCredentialsDto userCredentialsDto = UserCredentialsDto.builder()
                .email(email)
                .password("currentPassword")
                .roles(Set.of("ADMIN"))
                .build();

        when(userService.findCredentialsByEmail(email)).thenReturn(Optional.of(userCredentialsDto));

        // When
        underTest.updatePromo(savePromoDto, email);

        // Then
        assertAll(
                () -> verify(promoRepository, times(1)).save(any())
        );
    }

    @Test
    void itShouldUpdatePromo() {
        // Given
        String email = "john@rambo.com";

        SavePromoDto savePromoDto = SavePromoDto.builder()
                .id(1L)
                .name("testPromo1")
                .description("best promo you ever saw 1")
                .price(new BigDecimal("99.99"))
                .category(CategoryType.ELECTRONICS.getDescription())
                .build();

        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Rambo")
                .email(email)
                .password("currentPassword")
                .roles(Collections.emptySet())
                .build();

        Promo promo = Promo.builder()
                .id(1L)
                .user(user)
                .name("testPromo1")
                .description("best promo you ever saw 1")
                .price(new BigDecimal("99.99"))
                .added(LocalDateTime.now().minusDays(1L))
                .category(new Category())
                .build();

        when(promoRepository.findPromosByUser_Email(email)).thenReturn(List.of(promo));
        when(promoDtoMapper.map(savePromoDto)).thenReturn(promo);

        // When
        underTest.updatePromo(savePromoDto, email);

        // Then
        assertAll(
                () -> verify(promoRepository, times(1)).save(any())
        );
    }

    @Test
    void itShouldThrow403ExceptionWhenUserTryToUpdatePromo() {
        // Given
        String email = "john@rambo.com";

        SavePromoDto savePromoDto = SavePromoDto.builder()
                .id(1L)
                .name("testPromo1")
                .description("best promo you ever saw 1")
                .price(new BigDecimal("99.99"))
                .category(CategoryType.ELECTRONICS.getDescription())
                .build();

        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Rambo")
                .email(email)
                .password("currentPassword")
                .roles(Collections.emptySet())
                .build();

        Promo promo = Promo.builder()
                .id(1L)
                .user(user)
                .name("testPromo1")
                .description("best promo you ever saw 1")
                .price(new BigDecimal("99.99"))
                .added(LocalDateTime.now().minusDays(1L))
                .category(new Category())
                .build();

        when(promoRepository.findPromosByUser_Email(email)).thenReturn(Collections.emptyList());
        when(promoDtoMapper.map(savePromoDto)).thenReturn(promo);

        // When
        // Then
        assertAll(
                () -> assertThrows(ResponseStatusException.class, () -> underTest.updatePromo(savePromoDto, email))
        );
    }

    @Test
    void itShouldDeletePromoWhenUserHasAdminRole() {
        // Given
        String email = "john@rambo.com";

        UserCredentialsDto userCredentialsDto = UserCredentialsDto.builder()
                .email(email)
                .password("currentPassword")
                .roles(Set.of("ADMIN"))
                .build();

        Long promoId = 1L;
        Promo promo = Promo.builder()
                .id(promoId)
                .name("testPromo1")
                .description("best promo you ever saw 1")
                .price(new BigDecimal("99.99"))
                .added(LocalDateTime.now().minusDays(1L))
                .category(new Category())
                .build();

        when(promoRepository.findById(promoId)).thenReturn(Optional.of(promo));
        when(promoRepository.findPromosByUser_Email(email)).thenReturn(List.of(promo));
        when(userService.findCredentialsByEmail(email)).thenReturn(Optional.of(userCredentialsDto));

        // When
        underTest.deletePromo(promoId, email);

        // Then
        assertAll(
                () -> verify(promoRepository, times(1)).deleteById(promoId)
        );
    }

    @Test
    void itShouldDeletePromoWhenUserHasUserRoleAndPromoBelongsToThatUser() {
        // Given
        String email = "john@rambo.com";

        UserCredentialsDto userCredentialsDto = UserCredentialsDto.builder()
                .email(email)
                .password("currentPassword")
                .roles(Set.of("USER"))
                .build();

        Long promoId = 1L;
        Promo promo = Promo.builder()
                .id(promoId)
                .name("testPromo1")
                .description("best promo you ever saw 1")
                .price(new BigDecimal("99.99"))
                .added(LocalDateTime.now().minusDays(1L))
                .category(new Category())
                .build();

        when(promoRepository.findById(promoId)).thenReturn(Optional.of(promo));
        when(promoRepository.findPromosByUser_Email(email)).thenReturn(List.of(promo));
        when(userService.findCredentialsByEmail(email)).thenReturn(Optional.of(userCredentialsDto));

        // When
        underTest.deletePromo(promoId, email);

        // Then
        assertAll(
                () -> verify(promoRepository, times(1)).deleteById(promoId)
        );
    }

    @Test
    void itShouldThrow404NotFoundException() {
        // Given
        String email = "john@rambo.com";
        UserRole adminRole = UserRole.builder()
                .id(1L)
                .name("ADMIN")
                .description("Can log in service")
                .build();

        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Rambo")
                .email(email)
                .password("password")
                .roles(Set.of(adminRole))
                .build();

        Long promoId = 1L;

        Promo promo = Promo.builder()
                .id(promoId)
                .user(user)
                .name("testPromo1")
                .description("best promo you ever saw 1")
                .price(new BigDecimal("99.99"))
                .added(LocalDateTime.now().minusDays(1L))
                .category(new Category())
                .user(user)
                .build();

        when(promoRepository.findPromosByUser_Email(email)).thenReturn(List.of(promo));
        when(promoRepository.findById(promoId)).thenReturn(Optional.empty());

        // When
        // Then
        assertAll(
                () -> assertThrows(ResponseStatusException.class, () -> underTest.deletePromo(promoId, email))
        );
    }

    @Test
    void itShouldThrow403StatusExceptionWhenUserHasNoRole() {
        // Given
        String email = "john@rambo.com";
        UserRole noRole = UserRole.builder()
                .id(1L)
                .name("EMPTY_ROLE")
                .description("Can log in service")
                .build();

        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Rambo")
                .email(email)
                .password("password")
                .roles(Set.of(noRole))
                .build();

        Long promoId = 1L;

        Promo promo = Promo.builder()
                .id(promoId)
                .user(user)
                .name("testPromo1")
                .description("best promo you ever saw 1")
                .price(new BigDecimal("99.99"))
                .added(LocalDateTime.now().minusDays(1L))
                .category(new Category())
                .user(user)
                .build();

        UserCredentialsDto userCredentialsDto = UserCredentialsDto.builder()
                .email(email)
                .password("currentPassword")
                .roles(Set.of("NO_ROLE"))
                .build();

        when(promoRepository.findPromosByUser_Email(email)).thenReturn(List.of(promo));
        when(promoRepository.findById(promoId)).thenReturn(Optional.of(promo));
        when(userService.findCredentialsByEmail(email)).thenReturn(Optional.of(userCredentialsDto));

        // When
        // Then
        assertAll(
                () -> assertThrows(ResponseStatusException.class, () -> underTest.deletePromo(promoId, email))
        );
    }

    @Test
    void itShouldFindUserByGivenId() {
        // Given
        Long id = 1L;
        User user = User.builder()
                .id(id)
                .firstName("John")
                .lastName("Rambo")
                .email("john@rambo.com")
                .password("password")
                .roles(Collections.emptySet())
                .build();

        Promo promo = Promo.builder()
                .id(id)
                .user(user)
                .name("testPromo1")
                .description("best promo you ever saw 1")
                .price(new BigDecimal("99.99"))
                .build();

        PromoDto promoDto = PromoDto.builder()
                .id(id)
                .name("testPromo1")
                .description("best promo you ever saw 1")
                .price(new BigDecimal("99.99"))
                .build();

        when(promoRepository.findById(id)).thenReturn(Optional.of(promo));
        when(promoDtoMapper.map(promo)).thenReturn(promoDto);

        // When
        Optional<PromoDto> optionalPromoDto = underTest.findById(id);

        // Then
        assertAll(
                () -> assertThat(optionalPromoDto.isPresent()).isTrue()
        );
    }
}