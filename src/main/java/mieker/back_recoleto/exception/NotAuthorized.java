package mieker.back_recoleto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotAuthorized extends RuntimeException{
    public NotAuthorized(String message) {
        super(message);
    }
}
