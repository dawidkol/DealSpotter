package pl.dk.dealspotter.user;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import pl.dk.dealspotter.user.dto.UserDto;
import pl.dk.dealspotter.user.dto.UserRegistrationDto;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//@AutoConfigureMockMvc(addFilters = false)
@Log4j2
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init() {

    }
    @Test
    void shouldRegisterUser() {
        UserRegistrationDto userToRegister = UserRegistrationDto.builder()
                .firstName("Mariusz")
                .lastName("Kamiński")
                .email("m.kaminski@gmail.com")
                .password("simplePass")
                .build();

        userService.register(userToRegister);

        Optional<UserDto> userInDb = userService.findUsername("m.kaminski@gmail.com");

        assertAll(
                () -> assertThat(userInDb.orElseThrow().getFirstName()).isEqualTo("Mariusz"),
                () -> assertThat(userInDb.orElseThrow().getLastName()).isEqualTo("Kamiński"),
                () -> assertThat(userInDb.orElseThrow().getEmail()).isEqualTo("m.kaminski@gmail.com")
        );
    }

    @Test
    void shouldFindUserByEmail() {
        //('Mateusz', 'Kowalski', 'mateusz.kowalski@abc.pl', '{noop}simplePass')
        //given
        String userEmail = "mateusz.kowalski@abc.pl";

        //when
        Optional<UserDto> username = userService.findUsername(userEmail);

        //then
        assertAll(
                () -> assertThat(username.orElseThrow().getFirstName()).isEqualTo("Mateusz"),
                () -> assertThat(username.orElseThrow().getLastName()).isEqualTo("Kowalski"),
                () -> assertThat(username.orElseThrow().getEmail()).isEqualTo("mateusz.kowalski@abc.pl")
        );
    }

    @Test
    @WithMockUser(username = "admin@admin.pl", password = "admin")
    void shouldDeleteUserWithUserWhoHasAdminRole() {
        //given
        String userEmail = "mateusz.kowalski@abc.pl";

        //when
        userService.deleteUser(userEmail);

        //then
        assertThat(userService.findUsername(userEmail)).isEmpty();

    }

    @Test
    @WithMockUser(username = "admin@admin.pl", password = "admin")
    void shouldFindAllUserWithUserWhoHasAdminRole() {
        //given when
        List<UserDto> allUsers = userService.findAllUsers();

        assertThat(allUsers).hasSize(2);
    }

    @Test
    @WithMockUser(username = "mateusz.kowalski@abc.pl", password = "simplePass")
    void shouldThrowSecurityException() {
        SecurityException securityException = assertThrows(SecurityException.class, () -> {
            userService.findAllUsers();
        });

        String message = securityException.getMessage();
        String expectedMessage = "Brak uprawnień do strony";

        assertTrue(message.contains(expectedMessage));

    }

    @Test
    @WithMockUser(username = "admin@admin.pl", password = "admin")
    void shouldChangeAdminPassword() {
        //given
        String newPassword = "newHardPass";

        //when
        userService.changeUserPassword(newPassword);
        Optional<User> user = userRepository.findByEmail("admin@admin.pl");
        String newEncodedPassword = user.orElseThrow().getPassword();

        //then
        Assertions.assertTrue(passwordEncoder.matches(newPassword,newEncodedPassword));
    }

}
