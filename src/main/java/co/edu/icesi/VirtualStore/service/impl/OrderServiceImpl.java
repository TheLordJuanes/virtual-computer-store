package co.edu.icesi.VirtualStore.service.impl;

import co.edu.icesi.VirtualStore.dto.CartDTO;
import co.edu.icesi.VirtualStore.dto.CartItemDTO;
import co.edu.icesi.VirtualStore.model.Order;
import co.edu.icesi.VirtualStore.model.User;
import co.edu.icesi.VirtualStore.repository.OrderRepository;
import co.edu.icesi.VirtualStore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public void createOrder(User user, CartDTO cartDTO) {
        orderRepository.save(Order.builder().total(calculateTotalPrice(cartDTO)).status("CREATED").user(user).build());
    }

    @Override
    public List<Order> getOrdersByUserId(UUID userId) {
        return orderRepository.findByUserId(userId).orElse(null);
    }

    @Override
    public void removeOrder(UUID orderId) {
        orderRepository.findById(orderId).ifPresent(orderRepository::delete);
    }

    @Override
    public List<Order> getOrders() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public void modifyStatus(UUID orderId, String status){
        orderRepository.updateStatus(orderId, status);
    }

    private double calculateTotalPrice(CartDTO cartDTO) {
        double total = 0;
        for (CartItemDTO cartItemDTO: cartDTO.getItems())
            total += cartItemDTO.getPrice() * cartItemDTO.getQuantity();
        return total;
    }
}
