package co.edu.icesi.VirtualStore.dto;

import co.edu.icesi.VirtualStore.model.Role;
import lombok.*;

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

    private Role role;

    private CartDTO cart;

}