package pl.dk.dealspotter.user;

import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.dk.dealspotter.user.dto.UserCredentialsDto;
import pl.dk.dealspotter.user.dto.UserDto;
import pl.dk.dealspotter.user.dto.UserRegistrationDto;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    private static final String USER_ROLE = "USER";
    private static final String ADMIN_AUTHORITY = "ADMIN";
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserCredentialsDtoMapper userCredentialsDtoMapper;
    private final UserDtoMapper userDtoMapper;
    private final Validator validator;


    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository, PasswordEncoder passwordEncoder, UserCredentialsDtoMapper userCredentialsDtoMapper, UserDtoMapper userDtoMapper, Validator validator) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userCredentialsDtoMapper = userCredentialsDtoMapper;

        this.userDtoMapper = userDtoMapper;
        this.validator = validator;
    }

    public Optional<UserCredentialsDto> findCredentialsByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userCredentialsDtoMapper::userCredentialsDto);
    }

    @Transactional
    public void register(UserRegistrationDto userRegistrationDto) {
        User user = new User();
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        userRoleRepository.findByName(USER_ROLE).ifPresentOrElse(role -> user.getRoles().add(role),
                () -> {
                    throw new NoSuchElementException();
                }
        );
        userRepository.save(user);

    }

    @Transactional
    public void changeUserPassword(String newPassword) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUsername).orElseThrow();
        String newPasswordHash = passwordEncoder.encode(newPassword);
        currentUser.setPassword(newPasswordHash);

    }

    public Optional<UserDto> findUsername(String email) {
        return userRepository.findByEmail(email).map(userDtoMapper::map);
    }

    public Optional<List<UserDto>> findAllUsers() {
        if (checkAuthority()) {
            List<UserDto> users = ((List<User>) (userRepository.findAll()))
                    .stream()
                    .filter(user -> user.getRoles().stream().allMatch(x -> x.getName().equalsIgnoreCase(USER_ROLE)))
                    .map(userDtoMapper::map)
                    .toList();
            return Optional.ofNullable(users);
        }
        throw new SecurityException("Brak uprawnień do strony");
    }

    @Transactional
    public void deleteUser(String username) {
        if (checkAuthority()) {
            Long id = userRepository.findByEmail(username).orElseThrow(NoSuchElementException::new).getId();
            userRepository.deleteById(id);
        }
    }

    public boolean checkAuthority() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        UserCredentialsDto userCredentialsDto = findCredentialsByEmail(name).orElseThrow(NoSuchElementException::new);
        return userCredentialsDto.getRoles().stream().anyMatch(x -> x.equalsIgnoreCase(ADMIN_AUTHORITY) || x.equalsIgnoreCase(USER_ROLE));
    }

}
