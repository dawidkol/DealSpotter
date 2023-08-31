package pl.dk.dealspotter.user;

import org.springframework.stereotype.Service;
import pl.dk.dealspotter.user.dto.UserDto;

@Service
public class UserDtoMapper {

    public UserDto map(User user) {
        return UserDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .promo(user.getPromo())
                .build();
    }



/*    public User map(UserDto userDto) {
        return User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .promo(userDto.getPromo())
                .build();
    }*/
}
