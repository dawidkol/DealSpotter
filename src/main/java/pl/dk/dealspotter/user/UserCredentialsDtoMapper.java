package pl.dk.dealspotter.user;


import org.springframework.stereotype.Service;
import pl.dk.dealspotter.user.dto.UserCredentialsDto;

import java.util.Set;
import java.util.stream.Collectors;

@Service
class UserCredentialsDtoMapper {
    UserCredentialsDto userCredentialsDto(User user) {
        return UserCredentialsDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(getUserRoles(user))
                .build();
    }

    private static Set<String> getUserRoles(User user) {
        return user.getRoles().stream().map(UserRole::getName).collect(Collectors.toSet());
    }
}
