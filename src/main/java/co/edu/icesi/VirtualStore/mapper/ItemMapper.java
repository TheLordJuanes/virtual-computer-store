package co.edu.icesi.VirtualStore.mapper;

import co.edu.icesi.VirtualStore.dto.CartItemDTO;
import co.edu.icesi.VirtualStore.dto.ItemDTO;
import co.edu.icesi.VirtualStore.model.Item;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item fromDTO(ItemDTO itemDTO);

    CartItemDTO cartItemfromItem(Item item);

}
