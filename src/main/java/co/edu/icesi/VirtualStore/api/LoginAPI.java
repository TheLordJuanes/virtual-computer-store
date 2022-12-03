package co.edu.icesi.VirtualStore.api;

import co.edu.icesi.VirtualStore.dto.LoginDTO;
import co.edu.icesi.VirtualStore.dto.TokenDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/login")
public interface LoginAPI {

    @PostMapping
    TokenDTO login(@RequestBody LoginDTO loginDTO);
}