package thi.hexa.userservice.adapter.api.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnautherizedException extends RuntimeException {
    public UnautherizedException(String message) {
        super(message);
    }
}
