package co.edu.icesi.VirtualStore.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class CartDTO {
    private UUID id;
    private List<CartItemDTO> items;

    public CartDTO(){
        this.id = UUID.randomUUID();
        this.items = new ArrayList<>();
    }

}