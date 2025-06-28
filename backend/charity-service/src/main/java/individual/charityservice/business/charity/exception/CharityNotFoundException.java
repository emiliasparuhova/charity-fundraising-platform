package individual.charityservice.business.charity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CharityNotFoundException extends ResponseStatusException {
    public CharityNotFoundException() {super(HttpStatus.NOT_FOUND, "CHARITY_NOT_FOUND"); }
}
