package co.edu.icesi.VirtualStore.error.exception;

import co.edu.icesi.VirtualStore.constant.UserErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserError {

    private UserErrorCode code;
    private String message;
}