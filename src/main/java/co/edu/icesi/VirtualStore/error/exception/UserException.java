package co.edu.icesi.VirtualStore.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class UserException extends RuntimeException {

    private HttpStatus httpStatus;
    private UserError error;
}