package co.edu.icesi.VirtualStore.controller;

import co.edu.icesi.VirtualStore.constant.UserErrorCode;
import co.edu.icesi.VirtualStore.controller.mvc.AuthController;
import co.edu.icesi.VirtualStore.dto.*;
import co.edu.icesi.VirtualStore.error.exception.UserError;
import co.edu.icesi.VirtualStore.error.exception.UserException;
import co.edu.icesi.VirtualStore.mapper.UserMapper;
import co.edu.icesi.VirtualStore.model.Role;
import co.edu.icesi.VirtualStore.model.User;
import co.edu.icesi.VirtualStore.service.LoginService;
import co.edu.icesi.VirtualStore.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class AuthControllerTest {

    private AuthController authController;
    private UserService userService;
    private LoginService loginService;
    private UserMapper userMapper;
    private Model model;
    private HttpServletRequest httpServletRequest;
    private HttpSession httpSession;
    private BindingResult bindingResult;

    @BeforeEach
    public void init() {
        userService = mock(UserService.class);
        userMapper = mock(UserMapper.class);
        model = mock(Model.class);
        httpServletRequest = mock(HttpServletRequest.class);
        httpSession = mock(HttpSession.class);
        bindingResult = mock(BindingResult.class);
        loginService = mock(LoginService.class);
        authController = new AuthController(userService, loginService, userMapper);
    }

    @Test
    public void testLogout() {
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        doNothing().when(httpServletRequest).removeAttribute(any());
        assertEquals("redirect:/signIn", authController.logout(httpServletRequest));
        verify(httpServletRequest, times(1)).getSession();
        verify(httpSession, times(2)).removeAttribute(any());
    }

    @Test
    public void testSignInWithUserNotLogged() {
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(any())).thenReturn(null);
        when(model.addAttribute(any(), any())).thenReturn(model);
        assertEquals("login", authController.signIn(model, httpServletRequest));
        verify(httpServletRequest, times(1)).getSession();
        verify(httpSession, times(1)).getAttribute(any());
        verify(model, times(1)).addAttribute(any(), any());
    }

    @Test
    public void testSignInWithUserLogged() {
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(any())).thenReturn(new LoggedUserDTO(UUID.fromString("35a81e7d-342b-48e2-89e3-cccbb6e09f25"), "juan1234@icesi.edu.co", "Ju@nes1234", "Calle 34 #12-21", "+571234567890", new Role(), new CartDTO()));
        when(model.addAttribute(any(), any())).thenReturn(model);
        assertEquals("redirect:/home", authController.signIn(model, httpServletRequest));
        verify(httpServletRequest, times(1)).getSession();
        verify(httpSession, times(1)).getAttribute(any());
        verify(model, times(0)).addAttribute(any(), any());
    }

    @Test
    public void testLoginWithoutBindingErrorsSuccessfully() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(model.addAttribute(any(), any())).thenReturn(model);
        when(userMapper.loggedUserFromUser(any())).thenReturn(new LoggedUserDTO(null, "juanes1234@icesi.edu.co", "Ju@nes1234", "Calle 34 #12-21", "+571234567891", new Role(), new CartDTO()));
        when(userService.getUserByEmailOrPhoneNumber(any())).thenReturn(new User(UUID.fromString("35a81e7d-342b-48e2-89e3-cccbb6e09f25"), "juanes1234@icesi.edu.co", "Ju@nes1234", "Calle 34 #12-21", "+571234567891", new Role()));
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        doNothing().when(httpSession).setAttribute(any(), any());
        when(loginService.login(any())).thenReturn(new TokenDTO("35a81e7d-342b-48e2-89e3-cccbb6e09f25"));
        assertEquals("redirect:/home", authController.login(new LoginDTO(), bindingResult, model, httpServletRequest));
        verify(bindingResult, times(1)).hasErrors();
        verify(loginService, times(1)).login(any());
        verify(userMapper, times(1)).loggedUserFromUser(any());
        verify(userService, times(1)).getUserByEmailOrPhoneNumber(any());
        verify(httpSession, times(3)).setAttribute(any(), any());
        verify(httpServletRequest, times(1)).getSession();
    }

    @Test
    public void testLoginWithoutBindingErrorsNotSuccessfully() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(model.addAttribute(any(), any())).thenReturn(model);
        when(loginService.login(any())).thenThrow(new UserException(HttpStatus.BAD_REQUEST, new UserError(UserErrorCode.CODE_03, UserErrorCode.CODE_03.getMessage())));
        assertEquals("login", authController.login(new LoginDTO(), bindingResult, model, httpServletRequest));
        verify(bindingResult, times(1)).hasErrors();
        verify(loginService, times(1)).login(any());
        verify(userMapper, times(0)).loggedUserFromUser(any());
        verify(model, times(2)).addAttribute(any(), any());
    }

    @Test
    public void testLoginWithBindingErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(model.addAttribute(any(), any())).thenReturn(model);
        when(bindingResult.getFieldError()).thenReturn(new FieldError("", "", ""));
        assertEquals("login", authController.login(new LoginDTO(), bindingResult, model, httpServletRequest));
        verify(bindingResult, times(1)).hasErrors();
        verify(userService, times(0)).createUser(any());
        verify(userMapper, times(0)).fromDTO(any());
        verify(model, times(2)).addAttribute(any(), any());
    }

    @Test
    public void testSignUpWithUserNotLogged() {
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(any())).thenReturn(null);
        when(model.addAttribute(any(), any())).thenReturn(model);
        assertEquals("createUser", authController.signUp(model, httpServletRequest));
        verify(httpServletRequest, times(1)).getSession();
        verify(httpSession, times(1)).getAttribute(any());
        verify(model, times(1)).addAttribute(any(), any());
    }

    @Test
    public void testSignUpWithUserLogged() {
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        when(httpSession.getAttribute(any())).thenReturn(new LoggedUserDTO(UUID.fromString("35a81e7d-342b-48e2-89e3-cccbb6e09f25"), "juan1234@icesi.edu.co", "Ju@nes1234", "Calle 34 #12-21", "+571234567890", new Role(), new CartDTO()));
        when(model.addAttribute(any(), any())).thenReturn(model);
        assertEquals("redirect:/home", authController.signUp(model, httpServletRequest));
        verify(httpServletRequest, times(1)).getSession();
        verify(httpSession, times(1)).getAttribute(any());
        verify(model, times(0)).addAttribute(any(), any());
    }

    @Test
    public void testCreateUserWithoutBindingErrorsSuccessfully() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(model.addAttribute(any(), any())).thenReturn(model);
        when(userService.createUser(any())).thenReturn(new User());
        assertEquals("createUser", authController.createUser(new UserDTO(null,"juanes1234@icesi.edu.co", "Ju@nes1234", "Calle 34 #12-21", "+571234567891"), bindingResult, model));
        verify(bindingResult, times(1)).hasErrors();
        verify(userService, times(1)).createUser(any());
        verify(userMapper, times(1)).fromDTO(any());
        verify(model, times(1)).addAttribute(any(), any());
    }

    @Test
    public void testCreateUserWithoutBindingErrorsNotSuccessfully() {
        when(bindingResult.hasErrors()).thenReturn(false);
        when(model.addAttribute(any(), any())).thenReturn(model);
        when(userService.createUser(any())).thenThrow(new UserException(HttpStatus.BAD_REQUEST, new UserError(UserErrorCode.CODE_01, UserErrorCode.CODE_01.getMessage())));
        assertEquals("createUser", authController.createUser(new UserDTO(null,"juanes1234@icesi.edu.co", "Ju@nes1234", "Calle 34 #12-21", "+571234567891"), bindingResult, model));
        verify(bindingResult, times(1)).hasErrors();
        verify(userService, times(1)).createUser(any());
        verify(userMapper, times(1)).fromDTO(any());
        verify(model, times(2)).addAttribute(any(), any());
    }

    @Test
    public void testCreateUserWithBindingErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        when(model.addAttribute(any(), any())).thenReturn(model);
        when(bindingResult.getFieldError()).thenReturn(new FieldError("", "", ""));
        assertEquals("createUser", authController.createUser(new UserDTO(null,"juanes1234@gmail.com", "Juanes1234", "Calle 34 #12-21", "+531234567891"), bindingResult, model));
        verify(bindingResult, times(1)).hasErrors();
        verify(userService, times(0)).createUser(any());
        verify(userMapper, times(0)).fromDTO(any());
        verify(model, times(2)).addAttribute(any(), any());
    }
}