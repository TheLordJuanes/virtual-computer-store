package co.edu.icesi.VirtualStore.service;


import co.edu.icesi.VirtualStore.constant.UserErrorCode;
import co.edu.icesi.VirtualStore.error.exception.UserException;
import co.edu.icesi.VirtualStore.model.User;
import co.edu.icesi.VirtualStore.repository.RoleRepository;
import co.edu.icesi.VirtualStore.repository.UserRepository;
import co.edu.icesi.VirtualStore.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;

    private UserRepository userRepository;

    private User testUser;

    private final UUID testUserUUID = UUID.fromString("1887fcda-c227-400b-ad7c-541802a92d49");

    @BeforeEach
    void init(){
        userRepository = mock(UserRepository.class);
        RoleRepository roleRepository = mock(RoleRepository.class);
        userService = new UserServiceImpl(roleRepository,userRepository);

        testUser = new User(testUserUUID,"test@icesi.edu.co","Qwerty123#","C14","+573161234567",null);

    }

    @Test
    void testGetUser(){
        when(userRepository.findById(testUserUUID)).thenReturn(Optional.of(testUser));

        User user = userService.getUser(testUserUUID);

        verify(userRepository,times(1)).findById(testUserUUID);
        assertEquals(testUserUUID,user.getId());
    }

    @Test
    void testGetUserDoesNotExist(){
        when(userRepository.findById(testUserUUID)).thenReturn(Optional.empty());

        UserException thrown =
                assertThrows(UserException.class,
                        () -> userService.getUser(testUserUUID),
                        "User Exception expected");

        assertEquals(UserErrorCode.CODE_01.getMessage(),thrown.getError().getMessage());

        verify(userRepository,times(1)).findById(testUserUUID);
    }

    @Test
    void testModifyStatus(){

        doNothing().when(userRepository).updateUserToAdmin(testUser.getId());

        userService.makeAdmin(testUser.getId());

        verify(userRepository,times(1)).updateUserToAdmin(ArgumentMatchers.any());

    }

    @Test
    void testGetUserByEmail(){
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        User user = userService.getUserByEmailOrPhoneNumber(testUser.getEmail());

        verify(userRepository,times(1)).findByEmail(testUser.getEmail());
        assertEquals(testUserUUID,user.getId());
    }
    @Test
    void testGetUserByEmailDoesNotExist(){
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber(testUser.getEmail())).thenReturn(Optional.empty());

        assertNull(userService.getUserByEmailOrPhoneNumber(testUser.getEmail()));

        verify(userRepository,times(1)).findByEmail(testUser.getEmail());
        verify(userRepository,times(1)).findByPhoneNumber(testUser.getEmail());
    }

    @Test
    void testGetUserByPhone(){
        when(userRepository.findByEmail(testUser.getPhoneNumber())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber(testUser.getPhoneNumber())).thenReturn(Optional.of(testUser));

        User user = userService.getUserByEmailOrPhoneNumber(testUser.getPhoneNumber());

        verify(userRepository,times(1)).findByEmail(testUser.getPhoneNumber());
        verify(userRepository,times(1)).findByPhoneNumber(testUser.getPhoneNumber());
        assertEquals(testUserUUID,user.getId());
    }
    @Test
    void testGetUserByPhoneDoesNotExist(){
        when(userRepository.findByEmail(testUser.getPhoneNumber())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber(testUser.getPhoneNumber())).thenReturn(Optional.empty());

        assertNull(userService.getUserByEmailOrPhoneNumber(testUser.getPhoneNumber()));

        verify(userRepository,times(1)).findByEmail(testUser.getPhoneNumber());
        verify(userRepository,times(1)).findByPhoneNumber(testUser.getPhoneNumber());

    }

    @Test
    void testGetUsers(){
        when(userRepository.findAll()).thenReturn(Collections.singletonList(testUser));

        List<User> users = userService.getUsers();

        assertEquals(1,users.size());
        assertEquals(testUserUUID,users.get(0).getId());
        verify(userRepository,times(1)).findAll();
    }

    @Test
    void testGetUsersEmpty(){
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        List<User> users = userService.getUsers();

        assertEquals(0,users.size());
        verify(userRepository,times(1)).findAll();
    }

    @Test
    void testCreateUser(){
        when(userRepository.findByEmail(ArgumentMatchers.any())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber(ArgumentMatchers.any())).thenReturn(Optional.empty());
        when(userRepository.save(testUser)).thenReturn(testUser);

        User user = userService.createUser(testUser);

        verify(userRepository,times(1)).findByEmail(testUser.getEmail());
        verify(userRepository,times(1)).findByPhoneNumber(testUser.getPhoneNumber());
        verify(userRepository,times(1)).save(testUser);

        assertEquals(testUserUUID, user.getId());
    }

    @Test
    void testCreateUserAlreadyExistsEmail(){
        when(userRepository.findByEmail(ArgumentMatchers.any())).thenReturn(Optional.of(testUser));
        when(userRepository.findByPhoneNumber(ArgumentMatchers.any())).thenReturn(Optional.empty());

        UserException thrown =
                assertThrows(UserException.class,
                        () -> userService.createUser(testUser),
                        "User Exception expected");

        assertEquals(UserErrorCode.CODE_05.getMessage(),thrown.getError().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST,thrown.getHttpStatus());

        verify(userRepository,times(0)).save(testUser);
    }

    @Test
    void testCreateUserAlreadyExistsPhoneNumber(){
        when(userRepository.findByEmail(ArgumentMatchers.any())).thenReturn(Optional.empty());
        when(userRepository.findByPhoneNumber(ArgumentMatchers.any())).thenReturn(Optional.of(testUser));

        UserException thrown =
                assertThrows(UserException.class,
                        () -> userService.createUser(testUser),
                        "User Exception expected");

        assertEquals(UserErrorCode.CODE_05.getMessage(),thrown.getError().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST,thrown.getHttpStatus());

        verify(userRepository,times(0)).save(testUser);
    }

    @Test
    void testCreateUserAlreadyExistsEmailAndPhoneNumber(){
        when(userRepository.findByEmail(ArgumentMatchers.any())).thenReturn(Optional.of(testUser));
        when(userRepository.findByPhoneNumber(ArgumentMatchers.any())).thenReturn(Optional.of(testUser));

        UserException thrown =
                assertThrows(UserException.class,
                        () -> userService.createUser(testUser),
                        "User Exception expected");

        assertEquals(UserErrorCode.CODE_05.getMessage(),thrown.getError().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST,thrown.getHttpStatus());

        verify(userRepository,times(0)).save(testUser);
    }
}