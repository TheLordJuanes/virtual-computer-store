package co.edu.icesi.VirtualStore.api;

import co.edu.icesi.VirtualStore.dto.UserDTO;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RequestMapping("/users")
public interface UserAPI {

    @GetMapping("/{userId}")
    UserDTO getUser(@PathVariable() UUID userId);

    @PostMapping()
    UserDTO createUser(@RequestBody UserDTO userDTO) throws Exception;

    @GetMapping
    List<UserDTO> getUsers();
}