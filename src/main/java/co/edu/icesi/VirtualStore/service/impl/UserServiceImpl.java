package co.edu.icesi.VirtualStore.service.impl;

import co.edu.icesi.VirtualStore.constant.UserErrorCode;
import co.edu.icesi.VirtualStore.error.exception.UserError;
import co.edu.icesi.VirtualStore.error.exception.UserException;
import co.edu.icesi.VirtualStore.model.User;
import co.edu.icesi.VirtualStore.repository.RoleRepository;
import co.edu.icesi.VirtualStore.repository.UserRepository;
import co.edu.icesi.VirtualStore.service.UserService;
import co.edu.icesi.VirtualStore.service.utils.Encoder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    @Override
    public User getUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(HttpStatus.BAD_REQUEST, new UserError(UserErrorCode.CODE_01, UserErrorCode.CODE_01.getMessage())));
    }

    @Override
    public User getUserByEmailOrPhoneNumber(String param) {
        return userRepository.findByEmail(param).orElse(userRepository.findByPhoneNumber(param).orElse(null));
    }

    @Override
    public User createUser(User user) {
        validateUserExists(user.getEmail(), user.getPhoneNumber());
        user.setRole(roleRepository.getBasicUserRole());
        UUID randomUUID = UUID.randomUUID();
        user.setId(randomUUID);
        user.setPassword(Encoder.hashPassword(user.getPassword().toCharArray(), randomUUID.toString().getBytes()));
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        List<User> users = StreamSupport.stream(userRepository.findAll().spliterator(),false).collect(Collectors.toList());
        users.forEach(user -> user.setPassword(""));
        return users;
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public void makeAdmin(UUID userId){
        userRepository.updateUserToAdmin(userId);
    }

    @Override
    public void updateUserPassword(UUID id, String newPassword) {
        userRepository.updatePassword(id, Encoder.hashPassword(newPassword.toCharArray(), id.toString().getBytes()));
    }

    private void validateUserExists(String email, String phoneNumber) {
        if (userRepository.findByEmail(email).isPresent() || userRepository.findByPhoneNumber(phoneNumber).isPresent())
            throw new UserException(HttpStatus.BAD_REQUEST, new UserError(UserErrorCode.CODE_05, UserErrorCode.CODE_05.getMessage()));
    }
}
