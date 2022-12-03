package co.edu.icesi.VirtualStore.dto;

import co.edu.icesi.VirtualStore.model.Item;
import co.edu.icesi.VirtualStore.model.Order;

import java.util.UUID;

public class OrderItemDTO {

    private UUID id;

    private int quantity;

    private Item item;

    private Order order;
}
