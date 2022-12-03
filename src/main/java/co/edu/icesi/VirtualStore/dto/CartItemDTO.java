package co.edu.icesi.VirtualStore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {

    private UUID id;
    private String name;
    private String description;
    private double price;
    private String urlImage;
    private int quantity;

}
