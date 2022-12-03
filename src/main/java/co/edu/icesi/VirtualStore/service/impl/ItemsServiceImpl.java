package co.edu.icesi.VirtualStore.service.impl;

import co.edu.icesi.VirtualStore.constant.ItemErrorCode;
import co.edu.icesi.VirtualStore.model.Item;
import co.edu.icesi.VirtualStore.repository.ItemsRepository;
import co.edu.icesi.VirtualStore.service.ItemsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class ItemsServiceImpl implements ItemsService {

    private final ItemsRepository itemsRepository;

    @Override
    public List<Item> getItems() {
        return StreamSupport.stream(itemsRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public Item getItemByID(UUID itemId) {
        return itemsRepository.findById(itemId).orElse(null);
    }

    @Override
    public Item addItem(Item item) {
        return itemsRepository.save(item);
    }

    @Override
    public void modifyItem(UUID itemID, String attribute, String newValue) {
        itemsRepository.findById(itemID).orElseThrow(() -> new RuntimeException(ItemErrorCode.CODE_02.getMessage()));
        switch (attribute) {
            case "0":
                throw new RuntimeException(ItemErrorCode.CODE_03.getMessage());
            case "1":
                itemsRepository.updateItemName(itemID, newValue);
                break;
            case "2":
                itemsRepository.updateItemDescription(itemID, newValue);
                break;
            case "3":
                itemsRepository.updateItemPrice(itemID, Double.parseDouble(newValue));
                break;
        }
    }
}