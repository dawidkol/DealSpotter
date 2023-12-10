package pl.dk.dealspotter.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.dk.dealspotter.user.dto.UserCredentialsDto;
import pl.dk.dealspotter.user.dto.UserDto;
import pl.dk.dealspotter.user.dto.UserRegistrationDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private UserCredentialsDtoMapper userCredentialsDtoMapper;
    private UserDtoMapper userDtoMapper;
    private UserService underTest;

    @Captor
    ArgumentCaptor<User> userCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userCredentialsDtoMapper = new UserCredentialsDtoMapper();
        userDtoMapper = new UserDtoMapper();
        underTest = new UserService(userRepository, userRoleRepository, passwordEncoder, userCredentialsDtoMapper, userDtoMapper);
    }

    @Test
    void itShouldRegisterUser() {
        // Given
        UserRole userRole = UserRole.builder()
                .id(1L)
                .name("USER")
                .description("Can log in service")
                .build();

        UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder()
                .firstName("John")
                .lastName("Rambo")
                .email("bloodyjohn@test.pl")
                .password("rambo123")
                .build();

        User user = User.builder()
                .firstName("John")
                .lastName("Rambo")
                .email("bloodyjohn@test.pl")
                .password("rambo123")
                .roles(Set.of(userRole))
                .build();

        when(userRoleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(userRegistrationDto.getPassword())).thenReturn("rambo123");

        // When
        underTest.register(userRegistrationDto);

        // Then
        assertAll(
                () -> Mockito.verify(userRepository).save(userCaptor.capture()),
                () -> Mockito.verify(userRoleRepository).findByName("USER"),
                () -> assertThat(user).isEqualTo(userCaptor.getValue())
        );
    }

    @Test

    void itShouldThrowRoleNotFoundException() {
        // Given
        UserRole userRole = UserRole.builder()
                .id(1L)
                .name("USER")
                .description("Can log in service")
                .build();

        UserRegistrationDto userRegistrationDto = UserRegistrationDto.builder()
                .firstName("John")
                .lastName("Rambo")
                .email("bloodyjohn@test.pl")
                .password("rambo123")
                .build();

        // When
        when(userRoleRepository.findByName("USER")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userRegistrationDto.getPassword())).thenReturn("rambo123");

        // Then
        assertAll(
                () -> assertThrows(RoleNotFoundException.class, () -> underTest.register(userRegistrationDto)),
                () -> Mockito.verify(userRoleRepository).findByName("USER"),
                () -> Mockito.verify(passwordEncoder).encode(userRegistrationDto.getPassword())
        );
    }

    @Test
    void itShouldFindCredentialsByGivenEmail() {
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
                .password("rambo123")
                .roles(Set.of(userRole))
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        // When
        UserCredentialsDto userCredentialsDto = userCredentialsDtoMapper.userCredentialsDto(user);
        Optional<UserCredentialsDto> optionalUserCredentialsDto = underTest.findCredentialsByEmail(email);

        // Then
        assertAll(
                () -> Mockito.verify(userRepository).findByEmail(email),
                () -> assertThat(userCredentialsDto).isInstanceOf(UserCredentialsDto.class),
                () -> assertThat(optionalUserCredentialsDto).isPresent()
                        .hasValueSatisfying(c ->
                                assertThat(c).isEqualTo(userCredentialsDto)
                        ));
    }

    @Test
    void itShouldChangeUserPassword() {
        // Given
        String newPassword = "newPassword";
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

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(String.valueOf(user.getRoles()))
                .build();

        // set security context holder
        SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        emptyContext.setAuthentication(usernamePasswordAuthenticationToken);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);

        // When
        String currentUsername = emptyContext.getAuthentication().getName();
        underTest.changeUserPassword(newPassword, currentUsername);
        String changedPassword = user.getPassword();

        // Then

        assertAll (
                () -> verify(userRepository).findByEmail(email),
                () -> verify(passwordEncoder).encode(newPassword),
                () -> assertThat(user.getEmail()).isEqualTo(email),
                () -> assertThat(changedPassword).isEqualTo(newPassword)
        );
    }

    @Test
    void itShouldFindUsernameByGivenEmail() {
        // Given
        UserRole userRole = UserRole.builder()
                .id(1L)
                .name("USER")
                .description("Can log in service")
                .build();

        User user = User.builder()
                .firstName("John")
                .lastName("Rambo")
                .email("john@rambo.com")
                .password("currentPassword")
                .roles(Set.of(userRole))
                .build();

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // When
        Optional<UserDto> optionalUsername = underTest.findUser(user.getEmail());

        // Then
        assertAll(
                () -> verify(userRepository).findByEmail(user.getEmail()),
                () -> assertThat(optionalUsername).hasValueSatisfying(
                                u ->  {
                                    assertThat(u.getFirstName()).isEqualTo(user.getFirstName());
                                    assertThat(u.getLastName()).isEqualTo(user.getLastName());
                                    assertThat(u.getEmail()).isEqualTo(user.getEmail());
                                }
                )
        );
    }

    @Test
    void itShouldFindAllUsersWhenUserHasAdminRole() {
        // Given
        UserRole userRole = UserRole.builder()
                .id(1L)
                .name("USER")
                .description("Can log in service")
                .build();

        User user = User.builder()
                .firstName("John")
                .lastName("Rambo")
                .email("john@rambo.com")
                .password("currentPassword")
                .roles(Set.of(userRole))
                .build();
        when(userRepository.findAll()).thenReturn(List.of(user));

        // When
        List<UserDto> allUsers = underTest.findAllUsers();
        // Then
        assertAll (
                () -> verify(userRepository).findAll(),
                () -> assertThat(allUsers.size()).isEqualTo(1),
                () -> assertThat(allUsers).isNotNull(),
                () -> assertThat(allUsers).isNotEmpty(),
                () -> assertThat(allUsers.get(0)).isInstanceOf(UserDto.class)
        );
    }

    @Test
    void itShouldDeleteCurrentUser() {
        // Given
        Long id = 1L;
        String email = "john@rambo.com";

        UserRole userRole = UserRole.builder()
                .id(1L)
                .name("USER")
                .description("Can log in service")
                .build();

        User user = User.builder()
                .id(id)
                .firstName("John")
                .lastName("Rambo")
                .email(email)
                .password("currentPassword")
                .roles(Set.of(userRole))
                .build();

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(String.valueOf(user.getRoles()))
                .build();

        // set security context holder
        SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        emptyContext.setAuthentication(usernamePasswordAuthenticationToken);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        underTest.deleteUser(email);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        // Then
        assertAll(
                () -> verify(userRepository).findByEmail(argumentCaptor.capture()),
                () -> verify(userRepository, times(1)).deleteById(id)
        );
    }
}