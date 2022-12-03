package co.edu.icesi.VirtualStore.api;


import co.edu.icesi.VirtualStore.dto.ItemDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/items")
public interface ItemsAPI {

    @GetMapping
    List<ItemDTO> getItems();

    @GetMapping("/{itemId}")
    ItemDTO getItem(@PathVariable UUID itemId);

    @PostMapping
    ItemDTO addItem(@RequestBody ItemDTO itemDTO);

}
