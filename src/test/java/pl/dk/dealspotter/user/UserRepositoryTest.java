package pl.dk.dealspotter.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @Test
    void itShouldFindUserByGivenEmail() {
        // Given
        String email = "test@email.pl";

        User testUser = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Rambo")
                .email(email)
                .build();

        underTest.save(testUser);
        // When
        Optional<User> optionalUser = underTest.findByEmail(email);

        // Then
        assertAll(
                () -> assertThat(optionalUser).isPresent(),
                () -> assertThat(optionalUser).hasValueSatisfying(
                        c -> assertThat(c).isEqualTo(testUser)
                )
        );
    }
}