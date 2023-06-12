package pl.dk.dealspotter.user;


import org.springframework.stereotype.Service;
import pl.dk.dealspotter.user.dto.UserCredentialsDto;

import java.util.Set;
import java.util.stream.Collectors;

@Service
class UserCredentialsDtoMapper {
    UserCredentialsDto userCredentialsDto(User user) {
        String email = user.getEmail();
        String password = user.getPassword();
        Set<String> roles = user.getRoles()
                .stream()
                .map(UserRole::getName)
                .collect(Collectors.toSet());
        return new UserCredentialsDto(email, password, roles);
    }
}
