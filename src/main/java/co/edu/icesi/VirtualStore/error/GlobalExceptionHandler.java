package co.edu.icesi.VirtualStore.error;

import co.edu.icesi.VirtualStore.constant.UserErrorCode;
import co.edu.icesi.VirtualStore.error.exception.UserError;
import co.edu.icesi.VirtualStore.error.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<UserError> handleException(UserException tatabroException) {
        return new ResponseEntity<>(tatabroException.getError(), tatabroException.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<UserError> handleException(MethodArgumentNotValidException methodArgumentNotValidException) {
        UserException userException = new UserException(HttpStatus.BAD_REQUEST, new UserError(UserErrorCode.UNIVERSAL_ANNOTATION_CODE, Objects.requireNonNull(methodArgumentNotValidException.getFieldError()).getDefaultMessage()));
        return new ResponseEntity<>(userException.getError(), userException.getHttpStatus());
    }
}