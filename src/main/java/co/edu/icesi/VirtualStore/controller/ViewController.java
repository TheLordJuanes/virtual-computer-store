package co.edu.icesi.VirtualStore.controller;

import co.edu.icesi.VirtualStore.dto.*;
import co.edu.icesi.VirtualStore.mapper.ItemMapper;
import co.edu.icesi.VirtualStore.mapper.UserMapper;
import co.edu.icesi.VirtualStore.model.Order;
import co.edu.icesi.VirtualStore.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class ViewController {

    private final UserService userService;
    private final ItemsService itemsService;

    private final UserMapper userMapper;
    private final ItemMapper itemMapper;
    private final OrderService orderService;

    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        model.addAttribute("role", session.getAttribute("role").toString());
        model.addAttribute("items", itemsService.getItems().stream().map(itemMapper::cartItemfromItem).collect(Collectors.toList()));
        return "home";
    }

    @PostMapping("/addCartItem")
    public String addCartItem(CartItemDTO item, HttpServletRequest request){
        HttpSession session = request.getSession();
        LoggedUserDTO loggedUserDTO = ((LoggedUserDTO) session.getAttribute("LoggedUser"));
        loggedUserDTO.getCart().getItems().add(item);
        return "redirect:/home";
    }


    @GetMapping("/getUsers")
    public String getUsers(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (((LoggedUserDTO) session.getAttribute("LoggedUser")).getRole().getName().equals("Admin")) {
            model.addAttribute("users", userService.getUsers());
            return "getUsers";
        }
        return "redirect:/home";
    }

    @PostMapping("/makeAdmin")
    public String makeAdmin(@RequestParam("id") UUID userId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (((LoggedUserDTO) session.getAttribute("LoggedUser")).getRole().getName().equals("Admin")) {
            userService.makeAdmin(userId);
            return "redirect:/getUsers";
        }
        return "redirect:/home";
    }

    @GetMapping("/getCart")
    public String getCart(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        LoggedUserDTO loggedUserDTO = ((LoggedUserDTO) session.getAttribute("LoggedUser"));
        model.addAttribute("items", loggedUserDTO.getCart().getItems());
        return "cart";
    }

    @PostMapping("/removeItem")
    public String removeCartItem(CartItemDTO item, HttpServletRequest request) {
        HttpSession session = request.getSession();
        LoggedUserDTO loggedUserDTO = ((LoggedUserDTO) session.getAttribute("LoggedUser"));
        loggedUserDTO.getCart().getItems().removeIf(cartItemDTO -> (cartItemDTO.getId().equals(item.getId())));
        return "redirect:/getCart";
    }

    @PostMapping("/removeOrder")
    public String removeOrder(Order order) {
        orderService.removeOrder(order.getId());
        return "redirect:/getOrders";
    }

    @GetMapping("/createOrder")
    public String createOrder(HttpServletRequest request) {
        HttpSession session = request.getSession();
        LoggedUserDTO loggedUserDTO = ((LoggedUserDTO) session.getAttribute("LoggedUser"));
        orderService.createOrder(userMapper.fromLoggedUserDTO(loggedUserDTO), loggedUserDTO.getCart());
        loggedUserDTO.setCart(new CartDTO());
        return "redirect:/getOrders";
    }

    @GetMapping("/getOrders")
    public String getOrders(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        LoggedUserDTO loggedUserDTO = ((LoggedUserDTO) session.getAttribute("LoggedUser"));
        model.addAttribute("role", session.getAttribute("role").toString());
        if (loggedUserDTO.getRole().getName().equals("Admin"))
            model.addAttribute("orders", orderService.getOrders());
        else
            model.addAttribute("orders", orderService.getOrdersByUserId(loggedUserDTO.getId()));
        return "order";
    }

    @PostMapping("/modifyStatus")
    public String modifyStatus(@RequestParam("id") UUID orderId, @RequestParam("status") String status, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (((LoggedUserDTO) session.getAttribute("LoggedUser")).getRole().getName().equals("Admin")) {
            orderService.modifyStatus(orderId, status);
            System.out.println(orderId + "    " + status);
            return "redirect:/getOrders";
        }
        return "redirect:/home";
    }

    @GetMapping("/createNewItem")
    public String createItem(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (((LoggedUserDTO) session.getAttribute("LoggedUser")).getRole().getName().equals("Admin")) {
            model.addAttribute("itemDTO", new ItemDTO());
            return "createItem";
        }
        return "redirect:/home";
    }

    @PostMapping("/createItem")
    public String createItem(@Valid @ModelAttribute ItemDTO itemDTO, BindingResult errors, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (((LoggedUserDTO) session.getAttribute("LoggedUser")).getRole().getName().equals("Admin")) {
            if (hasBindingErrors(errors, model)) {
                try {
                    itemsService.addItem(itemMapper.fromDTO(itemDTO));
                    model.addAttribute("itemResponse", true);
                } catch (RuntimeException runtimeException) {
                    model.addAttribute("itemResponse", false);
                    model.addAttribute("message", runtimeException.getMessage());
                }
            }
            return "createItem";
        }
        return "redirect:/home";
    }

    @GetMapping("/modifyItem")
    public String modifyItem(@RequestParam("itemId") UUID itemId, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (((LoggedUserDTO) session.getAttribute("LoggedUser")).getRole().getName().equals("Admin")) {
            model.addAttribute("itemId", itemId);
            return "modifyItem";
        }
        return "redirect:/home";
    }

    @PostMapping("/updateItem")
    public String modifyItem(@RequestParam(value = "itemID", required = false) UUID itemID, @RequestParam(value = "attribute", required = false) String attribute, @RequestParam(value = "newValue", required = false) String newValue, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (((LoggedUserDTO) session.getAttribute("LoggedUser")).getRole().getName().equals("Admin")) {
            try {
                itemsService.modifyItem(itemID, attribute, newValue);
                model.addAttribute("itemResponse", true);
            } catch (RuntimeException runtimeException) {
                model.addAttribute("itemResponse", false);
                model.addAttribute("attribute", attribute);
                model.addAttribute("message", runtimeException.getMessage());
            }
            return "modifyItem";
        }
        return "redirect:/home";
    }

    private boolean hasBindingErrors(BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("itemResponse", false);
            model.addAttribute("message", Objects.requireNonNull(errors.getFieldError()).getDefaultMessage());
            return false;
        }
        return true;
    }
}