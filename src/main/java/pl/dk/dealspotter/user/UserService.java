package pl.dk.dealspotter.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.dk.dealspotter.user.dto.UserCredentialsDto;
import pl.dk.dealspotter.user.dto.UserDto;
import pl.dk.dealspotter.user.dto.UserRegistrationDto;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String USER_ROLE = "USER";
    private static final String ADMIN_AUTHORITY = "ADMIN";
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
    public void changeUserPassword(String newPassword) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(UserNotFoundException::new);
        String newPasswordHash = passwordEncoder.encode(newPassword);
        currentUser.setPassword(newPasswordHash);
    }

    public Optional<UserDto> findUsername(String email) {
        return userRepository.findByEmail(email).map(userDtoMapper::map);
    }

    public List<UserDto> findAllUsers() {
        if (checkAuthority()) {
            return ((List<User>) (userRepository.findAll()))
                    .stream()
                    .filter(user -> user.getRoles().stream().allMatch(x -> x.getName().equalsIgnoreCase(USER_ROLE)))
                    .map(userDtoMapper::map)
                    .toList();
        } else {
            throw new SecurityException("Brak uprawnień do strony");
        }
    }

    public void deleteUser(String email) {
        if (checkAuthority()) {
            Long id = userRepository.findByEmail(email)
                    .orElseThrow(UserNotFoundException::new)
                    .getId();
            userRepository.deleteById(id);
        } else {
            throw new SecurityException();
        }
    }

    private boolean checkAuthority() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserCredentialsDto userCredentialsDto = findCredentialsByEmail(name).orElseThrow(UserNotFoundException::new);
        return userCredentialsDto.getRoles().stream().anyMatch(x -> x.equalsIgnoreCase(ADMIN_AUTHORITY) /*|| x.equalsIgnoreCase(USER_ROLE)*/);
    }
}
