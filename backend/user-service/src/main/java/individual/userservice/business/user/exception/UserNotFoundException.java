package individual.userservice.business.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserNotFoundException extends ResponseStatusException {
    public UserNotFoundException() {super(HttpStatus.BAD_REQUEST, "USER_NOT_FOUND"); }

}
