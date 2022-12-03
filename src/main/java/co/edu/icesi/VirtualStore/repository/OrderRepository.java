package co.edu.icesi.VirtualStore.repository;

import co.edu.icesi.VirtualStore.model.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends CrudRepository<Order, UUID> {

    Optional<List<Order>> findByUserId(UUID userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE orders SET status = ?2 WHERE order_id = ?1", nativeQuery = true)
    void updateStatus(UUID orderId, String status);
    
}