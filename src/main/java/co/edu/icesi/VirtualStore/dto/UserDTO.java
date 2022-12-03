package co.edu.icesi.VirtualStore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private UUID id;

    @Pattern(regexp = "\\w+\\.?\\w+@icesi\\.edu\\.co$", message = "Incorrect email format")
    private String email;

    @NotNull(message = "Attribute 'password' cannot be null.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[#$%@]).+$", message = "Incorrect password format")
    private String password;

    private String address;

    @Pattern(regexp = "^\\+57\\d{10}", message = "Incorrect phone number format")
    private String phoneNumber;
}