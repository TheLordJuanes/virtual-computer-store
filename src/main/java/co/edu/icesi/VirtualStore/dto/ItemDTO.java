package co.edu.icesi.VirtualStore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {

    private UUID id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    @DecimalMin(value = "0", message = "The item price cannot be negative.")
    private double price;

    @NotNull
    private String urlImage;
}
