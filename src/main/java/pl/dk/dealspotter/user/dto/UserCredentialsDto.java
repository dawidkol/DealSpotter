package pl.dk.dealspotter.user.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Set;


@Getter
@Builder
@EqualsAndHashCode
public class UserCredentialsDto {
    private final String email;
    private final String password;
    private final Set<String> roles;
}
