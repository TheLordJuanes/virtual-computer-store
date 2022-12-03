package co.edu.icesi.VirtualStore.dto;

import co.edu.icesi.VirtualStore.model.Item;

import javax.persistence.Id;
import java.util.UUID;

public class BasketItemDTO {

    @Id
    private UUID id;

    private Item item;

    private int quantity;

}
