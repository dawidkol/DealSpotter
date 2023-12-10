package pl.dk.dealspotter.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.dk.dealspotter.user.UserService;
import pl.dk.dealspotter.user.dto.UserCredentialsDto;

@Service
@AllArgsConstructor
class UserDetailsServiceImplementation implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findCredentialsByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("User with email %s not found", username)));
    }

    private UserDetails createUserDetails(UserCredentialsDto credentialsDto) {
        return User.builder()
                .username(credentialsDto.getEmail())
                .password(credentialsDto.getPassword())
                .roles(credentialsDto.getRoles().toArray(String[]::new))
                .build();
    }
}
