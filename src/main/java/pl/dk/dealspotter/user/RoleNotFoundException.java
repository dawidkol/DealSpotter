package pl.dk.dealspotter.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class RoleNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Podana rola użytkownika nie została odnaleziona";

    public RoleNotFoundException() {
        super(MESSAGE);
    }
}
