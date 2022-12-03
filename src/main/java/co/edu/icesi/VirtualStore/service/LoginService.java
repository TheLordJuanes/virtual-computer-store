package co.edu.icesi.VirtualStore.service;

import co.edu.icesi.VirtualStore.dto.LoginDTO;
import co.edu.icesi.VirtualStore.dto.TokenDTO;
import org.springframework.web.bind.annotation.RequestBody;

public interface LoginService {

    TokenDTO login(@RequestBody LoginDTO loginDTO);
}