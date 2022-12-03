package co.edu.icesi.VirtualStore.service;

import co.edu.icesi.VirtualStore.dto.CartDTO;
import co.edu.icesi.VirtualStore.dto.CartItemDTO;
import co.edu.icesi.VirtualStore.model.Order;
import co.edu.icesi.VirtualStore.model.User;
import co.edu.icesi.VirtualStore.repository.OrderRepository;
import co.edu.icesi.VirtualStore.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    private OrderService orderService;

    private OrderRepository orderRepository;

    private UUID[] testItemsUUIDs = {UUID.fromString("1887fcda-c227-400b-ad7c-541802a92d74")
                                    ,UUID.fromString("1887fcda-c227-400b-ad7c-541802a92d56")};

    private CartDTO cartDTO;

    private double cartTotal;

    private User testUser;

    private UUID testUserUUID = UUID.fromString("1887fcda-c227-400b-ad7c-541802a92d49");

    private Order testOrder;

    @BeforeEach
    void init(){
        orderRepository = mock(OrderRepository.class);
        orderService = new OrderServiceImpl(orderRepository);

        testUser = new User(testUserUUID,"test@icesi.edu.co","Qwerty123#","C14","+573161234567",null);

    }

    void createTestOrder(){
        cartDTO = new CartDTO();

        CartItemDTO cartItemDTO1 = new CartItemDTO(testItemsUUIDs[0],"name1","description",5000.0,"MYURL",2);
        CartItemDTO cartItemDTO2 = new CartItemDTO(testItemsUUIDs[1],"name2","description2",2500.0,"THIS_IS_MY_URL",2);

        cartDTO.getItems().add(cartItemDTO1);
        cartDTO.getItems().add(cartItemDTO2);

        cartTotal = cartItemDTO1.getPrice()*cartItemDTO1.getQuantity() + cartItemDTO2.getPrice()*cartItemDTO2.getQuantity();

        testOrder = Order.builder().user(testUser).total(cartTotal).status("CREATED").build();
    }

    @Test
    void testCreateOrder(){

        createTestOrder();

        when(orderRepository.save(ArgumentMatchers.any())).thenReturn(testOrder);

        orderService.createOrder(testUser, cartDTO);

        ArgumentCaptor<Order> argumentCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(argumentCaptor.capture());
        verify(orderRepository,times(1)).save(ArgumentMatchers.any());

        assertEquals(testUser.getId(),argumentCaptor.getValue().getUser().getId());
        assertEquals(cartTotal,argumentCaptor.getValue().getTotal());
        assertEquals(testOrder.getStatus(),argumentCaptor.getValue().getStatus());

    }

    @Test
    void testGetOrdersByUserId(){
        createTestOrder();

        when(orderRepository.findByUserId(ArgumentMatchers.any())).thenReturn(Optional.of(Collections.singletonList(testOrder)));

        List<Order> orders = orderService.getOrdersByUserId(testUserUUID);

        verify(orderRepository,times(1)).findByUserId(ArgumentMatchers.any());

        assertEquals(1,orders.size());
        assertEquals(testOrder.getId(),orders.get(0).getId());

    }

    @Test
    void testGetOrdersByUserIdOrderDoesNotExists(){
        createTestOrder();

        when(orderRepository.findByUserId(ArgumentMatchers.any())).thenReturn(Optional.empty());

        List<Order> orders = orderService.getOrdersByUserId(testUserUUID);

        verify(orderRepository,times(1)).findByUserId(ArgumentMatchers.any());

        assertNull(orders);

    }

    @Test
    void testRemoveOrder(){
        createTestOrder();

        when(orderRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(testOrder));
        doNothing().when(orderRepository).deleteById(testOrder.getId());

        orderService.removeOrder(testOrder.getId());

        verify(orderRepository,times(1)).deleteById(ArgumentMatchers.any());

    }

    @Test
    void testModifyStatus(){
        createTestOrder();

        doNothing().when(orderRepository).updateStatus(testOrder.getId(), "SENT");

        orderService.modifyStatus(testOrder.getId(), "SENT");

        verify(orderRepository,times(1)).updateStatus(ArgumentMatchers.any(), ArgumentMatchers.any());

    }

    @Test
    void testGetOrders(){
        createTestOrder();

        when(orderRepository.findAll()).thenReturn(List.of(testOrder));

        List<Order> orders = orderService.getOrders();

        verify(orderRepository,times(1)).findAll();
        assertEquals(1, orders.size());

    }

    @Test
    void testRemoveOrderStatusNotRemovable(){
        createTestOrder();

        testOrder.setStatus("SENT");

        when(orderRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(testOrder));

        orderService.removeOrder(testOrder.getId());

        verify(orderRepository,times(0)).deleteById(ArgumentMatchers.any());

    }

    @Test
    void testRemoveOrderDoesNotExist(){
        createTestOrder();

        when(orderRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());

        orderService.removeOrder(testOrder.getId());

        verify(orderRepository,times(0)).deleteById(ArgumentMatchers.any());

    }

}
