package pl.dk.dealspotter.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.dk.dealspotter.user.dto.UserCredentialsDto;
import pl.dk.dealspotter.user.dto.UserDto;
import pl.dk.dealspotter.user.dto.UserRegistrationDto;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserCredentialsDtoMapper userCredentialsDtoMapper;
    private final UserDtoMapper userDtoMapper;

    public Optional<UserCredentialsDto> findCredentialsByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userCredentialsDtoMapper::userCredentialsDto);
    }

    @Transactional
    public void register(UserRegistrationDto userRegistrationDto) {
        User user = User.builder()
                .firstName(userRegistrationDto.getFirstName())
                .lastName(userRegistrationDto.getLastName())
                .email(userRegistrationDto.getEmail())
                .password(passwordEncoder.encode(userRegistrationDto.getPassword()))
                .roles(new HashSet<>())
                .build();

        userRoleRepository.findByName(USER_ROLE).ifPresentOrElse(role -> user.getRoles().add(role),
                () -> {
                    throw new RoleNotFoundException();
                });
        userRepository.save(user);
    }

    @Transactional
    public void changeUserPassword(String newPassword, String currentUsername) {
        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(UserNotFoundException::new);
        String newPasswordHash = passwordEncoder.encode(newPassword);
        currentUser.setPassword(newPasswordHash);
    }

    public Optional<UserDto> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userDtoMapper::map);
    }

    public List<UserDto> findAllUsers() {
        return userRepository.findAllByRolesNameIgnoreCase(USER_ROLE)
                .stream()
                .map(userDtoMapper::map)
                .toList();
    }

    public void deleteUser(String email) {
        Long id = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new)
                .getId();
        userRepository.deleteById(id);

    }

    public boolean checkCredentials(String email, String role) {
        return this.findCredentialsByEmail(email)
                .stream()
                .map(UserCredentialsDto::getRoles)
                .flatMap(Set::stream)
                .anyMatch(c -> c.equalsIgnoreCase(role));
    }
}
