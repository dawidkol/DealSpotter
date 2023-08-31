package pl.dk.dealspotter.user.dto;

import lombok.Builder;
import lombok.Getter;
import pl.dk.dealspotter.promo.Promo;

import java.util.List;



@Builder
@Getter
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private List<Promo> promo;
}

