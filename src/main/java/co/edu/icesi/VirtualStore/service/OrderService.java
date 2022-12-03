package co.edu.icesi.VirtualStore.service;

import co.edu.icesi.VirtualStore.dto.CartDTO;
import co.edu.icesi.VirtualStore.model.Order;
import co.edu.icesi.VirtualStore.model.User;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    void createOrder(User user, CartDTO cartDTO);

    List<Order> getOrdersByUserId(UUID userId);

    List<Order> getOrders();

    void removeOrder(UUID orderId);

    void modifyStatus(UUID orderId, String status);

}
