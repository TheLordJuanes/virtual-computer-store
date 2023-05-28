package co.edu.icesi.VirtualStore.controller;

import co.edu.icesi.VirtualStore.dto.*;
import co.edu.icesi.VirtualStore.mapper.ItemMapper;
import co.edu.icesi.VirtualStore.mapper.UserMapper;
import co.edu.icesi.VirtualStore.model.Order;
import co.edu.icesi.VirtualStore.model.User;
import co.edu.icesi.VirtualStore.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
    private final OrderService orderService;

    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        model.addAttribute("role", session.getAttribute("role").toString());
        model.addAttribute("items", itemsService.getItems().stream().map(itemMapper::cartItemfromItem).collect(Collectors.toList()));
        model.addAttribute("lastLogin", ((LoggedUserDTO) session.getAttribute("LoggedUser")).getLastLogin());
        return "home";
    }

    @PostMapping("/addCartItem")
    public String addCartItem(CartItemDTO item, HttpServletRequest request) {
        HttpSession session = request.getSession();
        LoggedUserDTO loggedUserDTO = ((LoggedUserDTO) session.getAttribute("LoggedUser"));
        if (loggedUserDTO.getRole().getName().equals("Basic")) {
            loggedUserDTO.getCart().getItems().add(item);
            return "redirect:/home";
        }
        return "redirect:/home";
    }


    @GetMapping("/getUsers")
    public String getUsers(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        if (((LoggedUserDTO) session.getAttribute("LoggedUser")).getRole().getName().equals("Admin")) {
            model.addAttribute("users", userService.getUsers());
            if (redirectAttributes.containsAttribute("passwordResponse")) {
                Boolean passwordResponse = (Boolean) redirectAttributes.getFlashAttributes().get("passwordResponse");
                model.addAttribute("passwordResponse", passwordResponse);
            }
            if (redirectAttributes.containsAttribute("message")) {
                String message = (String) redirectAttributes.getFlashAttributes().get("message");
                model.addAttribute("message", message);
            }
            return "getUsers";
        }
        return "redirect:/home";
    }

    @GetMapping("/getProfile")
    public String getProfile(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        LoggedUserDTO loggedUserDTO = ((LoggedUserDTO) session.getAttribute("LoggedUser"));
        if (loggedUserDTO.getRole().getName().equals("Basic")) {
            model.addAttribute("userProfile", loggedUserDTO);
            return "profile";
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

    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable UUID id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (((LoggedUserDTO) session.getAttribute("LoggedUser")).getRole().getName().equals("Admin")) {
            List<Order> orders = orderService.getOrdersByUserId(id);
            Optional.ofNullable(orders).ifPresent(orderList -> orderList.forEach(order -> orderService.removeOrder(order.getId())));
            userService.deleteUser(id);
            return "redirect:/getUsers";
        }
        return "redirect:/home";
    }

    @PostMapping("/resetPassword/{id}")
    public String resetPassword(@PathVariable UUID id, @ModelAttribute User user, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        if (((LoggedUserDTO) session.getAttribute("LoggedUser")).getRole().getName().equals("Admin")) {
            if (user.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[#$%@]).+$")) {
                if (!user.getPassword().equals(userService.getUser(id).getPassword())) {
                    userService.updateUserPassword(id, user.getPassword());
                    redirectAttributes.addFlashAttribute("passwordResponse", true);
                } else {
                    redirectAttributes.addFlashAttribute("passwordResponse", false);
                    redirectAttributes.addFlashAttribute("message", "Password must be different from the current one");
                }
            } else {
                redirectAttributes.addFlashAttribute("passwordResponse", false);
                redirectAttributes.addFlashAttribute("message", "Password must contain at least one digit, one lowercase, one uppercase and one of these special characters #$%@");
            }
            return "redirect:/getUsers";
        }
        return "redirect:/home";
    }

    @GetMapping("/getCart")
    public String getCart(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        LoggedUserDTO loggedUserDTO = ((LoggedUserDTO) session.getAttribute("LoggedUser"));
        if (loggedUserDTO.getRole().getName().equals("Basic")) {
            model.addAttribute("items", loggedUserDTO.getCart().getItems());
            return "cart";
        }
        return "redirect:/home";
    }

    @PostMapping("/removeItem")
    public String removeCartItem(CartItemDTO item, HttpServletRequest request) {
        HttpSession session = request.getSession();
        LoggedUserDTO loggedUserDTO = ((LoggedUserDTO) session.getAttribute("LoggedUser"));
        if (loggedUserDTO.getRole().getName().equals("Basic")) {
            loggedUserDTO.getCart().getItems().removeIf(cartItemDTO -> (cartItemDTO.getId().equals(item.getId())));
            return "redirect:/getCart";
        }
        return "redirect:/home";
    }

    @PostMapping("/removeOrder")
    public String removeOrder(Order order, HttpServletRequest request) {
        HttpSession session = request.getSession();
        LoggedUserDTO loggedUserDTO = ((LoggedUserDTO) session.getAttribute("LoggedUser"));
        if (loggedUserDTO.getRole().getName().equals("Basic")) {
            orderService.removeOrder(order.getId());
            return "redirect:/getOrders";
        }
        return "redirect:/home";
    }

    @GetMapping("/createOrder")
    public String createOrder(HttpServletRequest request) {
        HttpSession session = request.getSession();
        LoggedUserDTO loggedUserDTO = ((LoggedUserDTO) session.getAttribute("LoggedUser"));
        if (loggedUserDTO.getRole().getName().equals("Basic")) {
            orderService.createOrder(userMapper.fromLoggedUserDTO(loggedUserDTO), loggedUserDTO.getCart());
            loggedUserDTO.setCart(new CartDTO());
            return "redirect:/getOrders";
        }
        return "redirect:/home";
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
    public String modifyStatus(@RequestParam("id") UUID orderId, @RequestParam("status") String status, HttpServletRequest request) {
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

    @GetMapping("/modifyPassword")
    public String updateUserPassword(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (((LoggedUserDTO) session.getAttribute("LoggedUser")).getRole().getName().equals("Basic"))
            return "updatePassword";
        return "redirect:/home";
    }

    @PostMapping("/updatePassword")
    public String updateUserPassword(@RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        LoggedUserDTO loggedUserDTO = ((LoggedUserDTO) session.getAttribute("LoggedUser"));
        if (loggedUserDTO.getRole().getName().equals("Basic")) {
            if (currentPassword.equals(loggedUserDTO.getPassword())) {
                if (newPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[#$%@]).+$")) {
                    if (newPassword.equals(confirmPassword)) {
                        if (!newPassword.equals(currentPassword)) {
                            userService.updateUserPassword(loggedUserDTO.getId(), newPassword);
                            loggedUserDTO.setPassword(newPassword);
                            model.addAttribute("passwordResponse", true);
                            return "updatePassword";
                        } else
                            model.addAttribute("message", "New password must be different from the current one");
                    }
                    else
                        model.addAttribute("message", "Passwords don't match");
                } else
                    model.addAttribute("message", "Password must contain at least one digit, one lowercase, one uppercase and one of these special characters #$%@");
            } else {
                model.addAttribute("message", "Current password is incorrect");
            }
            model.addAttribute("passwordResponse", false);
            return "updatePassword";
        }
        return "redirect:/home";
    }

    @PostMapping("/createItem")
    public String createItem(@Valid @ModelAttribute ItemDTO itemDTO, BindingResult errors, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (((LoggedUserDTO) session.getAttribute("LoggedUser")).getRole().getName().equals("Admin")) {
            if (hasNotBindingErrors(errors, model)) {
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

    private boolean hasNotBindingErrors(BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("itemResponse", false);
            model.addAttribute("message", Objects.requireNonNull(errors.getFieldError()).getDefaultMessage());
            return false;
        }
        return true;
    }
}
