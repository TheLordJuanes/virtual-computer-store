package co.edu.icesi.VirtualStore.dto;

import co.edu.icesi.VirtualStore.model.Role;
import lombok.*;

import javax.persistence.Column;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoggedUserDTO {

    private UUID id;

    private String email;

    private String password;

    private String address;

    private String phoneNumber;

    private String currentLogin;

    private String lastLogin;

    private Role role;

    private CartDTO cart;

}
