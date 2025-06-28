package individual.userservice.business.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailAlreadyInUseException extends ResponseStatusException {
    public EmailAlreadyInUseException() {super(HttpStatus.BAD_REQUEST, "EMAIL_IN_USE"); }
}
