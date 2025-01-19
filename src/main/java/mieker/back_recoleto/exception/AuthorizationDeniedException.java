package mieker.back_recoleto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AuthorizationDeniedException extends RuntimeException{
    public AuthorizationDeniedException(String message) {
        super(message);
    }
}
