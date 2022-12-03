package co.edu.icesi.VirtualStore.controller;

import co.edu.icesi.VirtualStore.api.UserAPI;
import co.edu.icesi.VirtualStore.dto.UserDTO;
import co.edu.icesi.VirtualStore.mapper.UserMapper;
import co.edu.icesi.VirtualStore.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class UserController implements UserAPI {

    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public UserDTO getUser(UUID userId) {
        return userMapper.fromUser(userService.getUser(userId));
    }

    @Override
    public UserDTO createUser(@Valid UserDTO userDTO) {
        return userMapper.fromUser(userService.createUser(userMapper.fromDTO(userDTO)));
    }

    @Override
    public List<UserDTO> getUsers() {
        return userService.getUsers().stream().map(userMapper::fromUser).collect(Collectors.toList());
    }
}