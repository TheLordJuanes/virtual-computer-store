package co.edu.icesi.VirtualStore.service.impl;

import co.edu.icesi.VirtualStore.constant.UserErrorCode;
import co.edu.icesi.VirtualStore.dto.LoginDTO;
import co.edu.icesi.VirtualStore.dto.TokenDTO;
import co.edu.icesi.VirtualStore.error.exception.UserError;
import co.edu.icesi.VirtualStore.error.exception.UserException;
import co.edu.icesi.VirtualStore.model.User;
import co.edu.icesi.VirtualStore.repository.UserRepository;
import co.edu.icesi.VirtualStore.service.LoginService;
import co.edu.icesi.VirtualStore.utils.JWTParser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;

    @Override
    public TokenDTO login(LoginDTO loginDTO) {
        User user = StreamSupport.stream(userRepository.findAll().spliterator(),false).filter(user1 -> user1.getEmail().equals(loginDTO.getEmailPhone())|| user1.getPhoneNumber().equals((loginDTO.getEmailPhone()))).findFirst().orElseThrow(() -> new UserException(HttpStatus.BAD_REQUEST, new UserError(UserErrorCode.CODE_03, UserErrorCode.CODE_03.getMessage())));
        if (user.getPassword().equals(loginDTO.getPassword())) {
            Map<String, String> claims = new HashMap<>();
            claims.put("userId", user.getId().toString());
            return new TokenDTO(JWTParser.createJWT(user.getId().toString(), user.getEmail(), user.getEmail(), claims,300000000000L));
        }
        throw new UserException(HttpStatus.BAD_REQUEST, new UserError(UserErrorCode.CODE_03, UserErrorCode.CODE_03.getMessage()));
    }

    @Override
    public void updateLastLogin(UUID userId, String lastLogin) {
        userRepository.updateLastLogin(userId, lastLogin);
    }
}
