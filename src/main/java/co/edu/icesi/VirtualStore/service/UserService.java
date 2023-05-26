package co.edu.icesi.VirtualStore.service;

import co.edu.icesi.VirtualStore.model.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.UUID;

public interface UserService {

    User getUser(@PathVariable UUID userId);

    User getUserByEmailOrPhoneNumber(@PathVariable String email);

    User createUser(@RequestBody User user);

    List<User> getUsers();

    void deleteUser(@PathVariable UUID userId);

    void makeAdmin(UUID userId);
}
