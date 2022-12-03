package co.edu.icesi.VirtualStore.controller.mvc;

import co.edu.icesi.VirtualStore.constant.UserErrorCode;
import co.edu.icesi.VirtualStore.dto.*;
import co.edu.icesi.VirtualStore.error.exception.UserError;
import co.edu.icesi.VirtualStore.error.exception.UserException;
import co.edu.icesi.VirtualStore.mapper.UserMapper;
import co.edu.icesi.VirtualStore.service.LoginService;
import co.edu.icesi.VirtualStore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final LoginService loginService;
    private final UserMapper userMapper;

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(HttpHeaders.AUTHORIZATION);
        session.removeAttribute("LoggedUser");
        return "redirect:/signIn";
    }

    @GetMapping("/signIn")
    public String signIn(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        LoggedUserDTO loggedUserDTO = ((LoggedUserDTO) session.getAttribute("LoggedUser"));
        if (loggedUserDTO == null) {
            model.addAttribute("loginDTO", new LoginDTO());
            return "login";
        }
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String login(@Valid @ModelAttribute LoginDTO loginDTO, BindingResult errors, Model model, HttpServletRequest request) {
        if (hasBindingErrors(errors, model, "loginResponse")) {
            try {
                TokenDTO tokenDTO = loginService.login(loginDTO);
                LoggedUserDTO loggedUserDTO = userMapper.loggedUserFromUser(userService.getUserByEmailOrPhoneNumber(loginDTO.getEmailPhone()));
                loggedUserDTO.setCart(new CartDTO());
                HttpSession session = request.getSession();
                session.setAttribute(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDTO.getToken());
                session.setAttribute("LoggedUser", loggedUserDTO);
                session.setAttribute("role", loggedUserDTO.getRole().getName());
                return "redirect:/home";
            } catch (UserException userException) {
                model.addAttribute("loginResponse", false);
                model.addAttribute("message", userException.getError().getMessage());
            }
        }
        return "login";
    }

    @GetMapping("/signUp")
    public String signUp(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        LoggedUserDTO loggedUserDTO = ((LoggedUserDTO) session.getAttribute("LoggedUser"));
        if (loggedUserDTO == null) {
            model.addAttribute("userDTO", new UserDTO());
            return "createUser";
        }
        return "redirect:/home";
    }

    @PostMapping("/register")
    public String createUser(@Valid @ModelAttribute UserDTO userDTO, BindingResult errors, Model model) {
        if (hasBindingErrors(errors, model, "userResponse")) {
            try {
                validateEmptyIdentifiers(userDTO);
                userService.createUser(userMapper.fromDTO(userDTO));
                model.addAttribute("userResponse", true);
            } catch (UserException userException) {
                model.addAttribute("userResponse", false);
                model.addAttribute("message", userException.getError().getMessage());
            }
        }
        return "createUser";
    }

    private void validateEmptyIdentifiers(UserDTO userDTO) {
        if (Optional.ofNullable(userDTO.getEmail()).isEmpty() && Optional.ofNullable(userDTO.getPhoneNumber()).isEmpty())
            throw new UserException(HttpStatus.BAD_REQUEST, new UserError(UserErrorCode.CODE_02, UserErrorCode.CODE_02.getMessage()));
    }

    private boolean hasBindingErrors(BindingResult errors, Model model, String attributeName) {
        if (errors.hasErrors()) {
            model.addAttribute(attributeName, false);
            model.addAttribute("message", Objects.requireNonNull(errors.getFieldError()).getDefaultMessage());
            return false;
        }
        return true;
    }
}