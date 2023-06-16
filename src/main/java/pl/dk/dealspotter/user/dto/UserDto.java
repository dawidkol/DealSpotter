package pl.dk.dealspotter.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.dk.dealspotter.promo.Promo;

import java.util.List;


@Getter
@Setter
@Builder
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private List<Promo> promo;


}

