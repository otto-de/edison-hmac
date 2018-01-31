package de.otto.edison.hmac;

import de.otto.hmac.authorization.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthorizationExceptionHandler {

    @ExceptionHandler(value = {AuthorizationException.class})
    protected ResponseEntity<Object> handleUnauthorized() {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
