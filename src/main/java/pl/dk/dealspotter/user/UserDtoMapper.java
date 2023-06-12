package pl.dk.dealspotter.user;

import org.springframework.stereotype.Service;
import pl.dk.dealspotter.user.dto.UserDto;

@Service
public class UserDtoMapper {

    public UserDto map(User user) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPromo(user.getPromo());
        return userDto;
    }
}
