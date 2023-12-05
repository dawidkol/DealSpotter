package pl.dk.dealspotter.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Użytkownik nie został odnaleziony";

    public UserNotFoundException() {
        super(MESSAGE);
    }
}
