package co.edu.icesi.VirtualStore.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class LoginDTO {

    @NotNull(message = "The email or phone number cannot be null.")
    @Pattern(regexp = "(\\w+\\.?\\w+@icesi\\.edu\\.co$)|(^\\+57\\d{10})", message = "The email must begin with alphanumeric characters (no special characters) followed by the @icesi.edu.co domain.")
    private String emailPhone;

    @NotNull(message = "Attribute 'password' cannot be null.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[#$%@]).+$", message = "Password must have at least 1 upper case letter, 1 lower case letter, a number and a symbol like #$%@")
    private String password;
}