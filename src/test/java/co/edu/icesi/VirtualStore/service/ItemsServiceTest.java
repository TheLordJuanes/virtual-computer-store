package co.edu.icesi.VirtualStore.service;

import co.edu.icesi.VirtualStore.constant.ItemErrorCode;
import co.edu.icesi.VirtualStore.model.Item;
import co.edu.icesi.VirtualStore.repository.ItemsRepository;
import co.edu.icesi.VirtualStore.service.impl.ItemsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ItemsServiceTest {

    private ItemsService itemsService;

    private ItemsRepository itemsRepository;

    private Item testItem;

    private final UUID testItemUUID = UUID.fromString("1887fcda-c227-400b-ad7c-541802a92d74");

    @BeforeEach
    void init(){
        itemsRepository = mock(ItemsRepository.class);
        itemsService = new ItemsServiceImpl(itemsRepository);
        testItem = new Item(testItemUUID,"item name","item description",1000.0,"url");
    }


    @Test
    void testAddItem(){
        when(itemsRepository.save(ArgumentMatchers.any())).thenReturn(testItem);
        Item returnedItem = itemsService.addItem(testItem);

        verify(itemsRepository,times(1)).save(testItem);
        assertEquals(testItem,returnedItem);
    }

    @Test
    void testGetItems(){
        when(itemsRepository.findAll()).thenReturn(Collections.singletonList(testItem));
        List<Item> items = itemsService.getItems();

        verify(itemsRepository,times(1)).findAll();
        assertEquals(1,items.size());
        assertEquals(testItemUUID,items.get(0).getId());

    }

    @Test
    void testGetItemById(){

        when(itemsRepository.findById(testItemUUID)).thenReturn(Optional.of(testItem));

        Item item = itemsService.getItemByID(testItemUUID);

        assertEquals(testItemUUID,item.getId());
        verify(itemsRepository,times(1)).findById(testItemUUID);
    }

    @Test
    void testGetItemByIdItemDoesNotExist(){
        when(itemsRepository.findById(testItemUUID)).thenReturn(Optional.empty());

        assertNull(itemsService.getItemByID(testItemUUID));
        verify(itemsRepository,times(1)).findById(testItemUUID);
    }

    @Test
    void testModifyItemName(){
        when(itemsRepository.findById(testItemUUID)).thenReturn(Optional.of(testItem));

        itemsService.modifyItem(testItemUUID,"1","updated name");

        doNothing().when(itemsRepository).updateItemName(testItemUUID,"updated name");

        verify(itemsRepository,times(1)).updateItemName(testItemUUID,"updated name");

    }

    @Test
    void testModifyItemDescription(){
        when(itemsRepository.findById(testItemUUID)).thenReturn(Optional.of(testItem));

        itemsService.modifyItem(testItemUUID,"2","updated description");

        doNothing().when(itemsRepository).updateItemDescription(testItemUUID,"updated description");

        verify(itemsRepository,times(1)).updateItemDescription(testItemUUID,"updated description");

    }

    @Test
    void testModifyItemPrice(){
        when(itemsRepository.findById(testItemUUID)).thenReturn(Optional.of(testItem));

        itemsService.modifyItem(testItemUUID,"3","5000");

        doNothing().when(itemsRepository).updateItemPrice(testItemUUID,5000);

        verify(itemsRepository,times(1)).updateItemPrice(testItemUUID,5000);

    }

    @Test
    void testModifyItemItemDoesNotExist(){

        when(itemsRepository.findById(testItemUUID)).thenReturn(Optional.empty());

        RuntimeException thrown =
                assertThrows(RuntimeException.class,
                        () -> itemsService.modifyItem(testItemUUID,"1","updatedName"),
                        "Runtime Exception expected");

        assertEquals(ItemErrorCode.CODE_02.getMessage(),thrown.getMessage());
    }

    @Test
    void testModifyItemInvalidAttribute(){

        when(itemsRepository.findById(testItemUUID)).thenReturn(Optional.of(testItem));

        RuntimeException thrown =
                assertThrows(RuntimeException.class,
                        () -> itemsService.modifyItem(testItemUUID,"0","a asdasd"),
                        "Runtime Exception expected");

        assertEquals(ItemErrorCode.CODE_03.getMessage(),thrown.getMessage());
    }
}
