package co.edu.icesi.VirtualStore.controller;

import co.edu.icesi.VirtualStore.dto.CartDTO;
import co.edu.icesi.VirtualStore.dto.CartItemDTO;
import co.edu.icesi.VirtualStore.dto.LoggedUserDTO;
import co.edu.icesi.VirtualStore.mapper.ItemMapper;
import co.edu.icesi.VirtualStore.mapper.UserMapper;
import co.edu.icesi.VirtualStore.mapper.UserMapperImpl;
import co.edu.icesi.VirtualStore.model.Order;
import co.edu.icesi.VirtualStore.model.Role;
import co.edu.icesi.VirtualStore.service.ItemsService;
import co.edu.icesi.VirtualStore.service.OrderService;
import co.edu.icesi.VirtualStore.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ViewControllerTest {

    private ViewController viewController;
    private UserService userService;
    private ItemsService itemsService;
    private OrderService orderService;
    private UserMapper userMapper;
    private ItemMapper itemMapper;
    private Model model;
    private HttpServletRequest httpServletRequest;
    private HttpSession httpSession;
    private RedirectAttributes redirectAttributes;

    private final UUID loggedUserId = UUID.fromString("1887fcda-c227-400b-ad7c-541802a92d49");

    private LoggedUserDTO testLoggedUserDTO;

    @BeforeEach
    public void init() {
        userService = mock(UserService.class);
        itemsService = mock(ItemsService.class);
        orderService = mock(OrderService.class);
        userMapper = new UserMapperImpl();
        itemMapper = mock(ItemMapper.class);
        model = mock(Model.class);
        httpServletRequest = mock(HttpServletRequest.class);
        httpSession = mock(HttpSession.class);
        redirectAttributes = mock(RedirectAttributes.class);
        viewController = new ViewController(userService, itemsService, orderService, userMapper, itemMapper);

        testLoggedUserDTO = LoggedUserDTO.builder().cart(new CartDTO()).id(loggedUserId).build();
        when(httpServletRequest.getSession()).thenReturn(httpSession);
    }

    @Test
    public void testHome() {
        when(model.addAttribute(any(), any())).thenReturn(model);
        when(httpSession.getAttribute(ArgumentMatchers.any())).thenReturn("");

        assertEquals("home", viewController.home(model, httpServletRequest));

        verify(model, times(2)).addAttribute(any(),any());
        verify(httpServletRequest, times(1)).getSession();
        verify(itemsService, times(1)).getItems();
    }

    @Test
    void testAddCartItem(){
        CartItemDTO cartItemDTO = new CartItemDTO();
        UUID cartItemDTOId = cartItemDTO.getId();

        when(httpSession.getAttribute("LoggedUser")).thenReturn(testLoggedUserDTO);

        assertEquals("redirect:/home", viewController.addCartItem(cartItemDTO, httpServletRequest));

        verify(httpServletRequest, times(1)).getSession();
        assertEquals(1,testLoggedUserDTO.getCart().getItems().size());
        assertEquals(cartItemDTOId,testLoggedUserDTO.getCart().getItems().get(0).getId());
    }

    @Test
    void testGetUsersAsAdmin(){
        testLoggedUserDTO.setRole(Role.builder().name("Admin").build());
        when(httpSession.getAttribute("LoggedUser")).thenReturn(testLoggedUserDTO);
        when(userService.getUsers()).thenReturn(Collections.singletonList(userMapper.fromLoggedUserDTO(testLoggedUserDTO)));

        assertEquals("getUsers", viewController.getUsers(model, httpServletRequest, redirectAttributes));
        verify(httpServletRequest, times(1)).getSession();
        verify(model,times(1)).addAttribute(any(),any());
    }

    @Test
    void testGetUsersAsBasic(){
        testLoggedUserDTO.setRole(Role.builder().name("Basic User").build());
        when(httpSession.getAttribute("LoggedUser")).thenReturn(testLoggedUserDTO);
        when(userService.getUsers()).thenReturn(Collections.singletonList(userMapper.fromLoggedUserDTO(testLoggedUserDTO)));

        assertEquals("redirect:/home", viewController.getUsers(model, httpServletRequest, redirectAttributes));
        verify(httpServletRequest, times(1)).getSession();
        verify(model,times(0)).addAttribute(any());
    }


    @Test
    void testGetCart(){
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setId(UUID.fromString("86ee5179-167a-4b99-a7fa-56a9349d41bc"));

        testLoggedUserDTO.getCart().getItems().add(cartItemDTO);

        when(httpSession.getAttribute("LoggedUser")).thenReturn(testLoggedUserDTO);
        when(model.addAttribute(any(),any())).thenReturn(model);


        assertEquals("redirect:/getCart", viewController.removeCartItem(cartItemDTO, httpServletRequest));
        verify(httpServletRequest, times(1)).getSession();
    }

    @Test
    void testRemoveCartItem(){
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setId(UUID.fromString("86ee5179-167a-4b99-a7fa-56a9349d41bc"));

        testLoggedUserDTO.getCart().getItems().add(cartItemDTO);

        when(httpSession.getAttribute("LoggedUser")).thenReturn(testLoggedUserDTO);

        assertEquals("redirect:/getCart", viewController.removeCartItem(cartItemDTO, httpServletRequest));
        verify(httpServletRequest, times(1)).getSession();
        assertTrue(testLoggedUserDTO.getCart().getItems().isEmpty());
    }
    @Test
    void testRemoveOrder(){
        doNothing().when(orderService).removeOrder(ArgumentMatchers.any());
        assertEquals("redirect:/getOrders", viewController.removeOrder(new Order(), httpServletRequest));
    }

    @Test
    void testCreateOrder(){

        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setId(UUID.fromString("86ee5179-167a-4b99-a7fa-56a9349d41bc"));

        doNothing().when(orderService).createOrder(ArgumentMatchers.any(),ArgumentMatchers.any());
        when(httpSession.getAttribute("LoggedUser")).thenReturn(testLoggedUserDTO);


        assertEquals("redirect:/getOrders", viewController.createOrder(httpServletRequest));
        verify(httpServletRequest, times(1)).getSession();
        assertEquals(0, testLoggedUserDTO.getCart().getItems().size());
    }


    @Test
    void testGetOrders(){
        testLoggedUserDTO.setRole(Role.builder().name("Basic User").build());
        when(httpSession.getAttribute("LoggedUser")).thenReturn(testLoggedUserDTO);
        when(httpSession.getAttribute("role")).thenReturn(testLoggedUserDTO.getRole());
        when(model.addAttribute(any(),any())).thenReturn(model);
        when(orderService.getOrdersByUserId(loggedUserId)).thenReturn(new ArrayList<>());

        assertEquals("order", viewController.getOrders(model,httpServletRequest));

        verify(httpServletRequest, times(1)).getSession();
    }

    @Test
    void testCreateItemViewAuthOK(){

        testLoggedUserDTO.setRole(Role.builder().name("Admin").build());
        when(httpSession.getAttribute("LoggedUser")).thenReturn(testLoggedUserDTO);
        when(model.addAttribute(any(),any())).thenReturn(model);

        assertEquals("createItem", viewController.createItem(model,httpServletRequest));
        verify(httpServletRequest, times(1)).getSession();
    }

    @Test
    void testCreateItemViewAuthNotOK(){

        testLoggedUserDTO.setRole(Role.builder().name("Basic").build());
        when(httpSession.getAttribute("LoggedUser")).thenReturn(testLoggedUserDTO);
        when(model.addAttribute(any(),any())).thenReturn(model);

        assertEquals("redirect:/home", viewController.createItem(model,httpServletRequest));
        verify(httpServletRequest, times(1)).getSession();
    }

    @Test
    void testModifyItemViewAuthOK(){

        testLoggedUserDTO.setRole(Role.builder().name("Admin").build());
        when(httpSession.getAttribute("LoggedUser")).thenReturn(testLoggedUserDTO);
        when(model.addAttribute(any(),any())).thenReturn(model);

        assertEquals("modifyItem", viewController.modifyItem(loggedUserId,model,httpServletRequest));
        verify(httpServletRequest, times(1)).getSession();
    }

    @Test
    void testModifyItemViewAuthNotOK(){

        testLoggedUserDTO.setRole(Role.builder().name("Basic").build());
        when(httpSession.getAttribute("LoggedUser")).thenReturn(testLoggedUserDTO);
        when(model.addAttribute(any(),any())).thenReturn(model);

        assertEquals("redirect:/home", viewController.modifyItem(loggedUserId,model,httpServletRequest));
        verify(httpServletRequest, times(1)).getSession();
    }
}
