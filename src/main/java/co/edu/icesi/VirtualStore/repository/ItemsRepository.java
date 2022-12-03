package co.edu.icesi.VirtualStore.repository;

import co.edu.icesi.VirtualStore.model.Item;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Repository
public interface ItemsRepository extends CrudRepository<Item, UUID> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE items SET name = ?2 WHERE item_id = ?1", nativeQuery = true)
    void updateItemName(UUID itemID, String newValue);

    @Transactional
    @Modifying
    @Query(value = "UPDATE items SET description = ?2 WHERE item_id = ?1", nativeQuery = true)
    void updateItemDescription(UUID itemID, String newValue);

    @Transactional
    @Modifying
    @Query(value = "UPDATE items SET price = ?2 WHERE item_id = ?1", nativeQuery = true)
    void updateItemPrice(UUID itemID, double newValue);
}