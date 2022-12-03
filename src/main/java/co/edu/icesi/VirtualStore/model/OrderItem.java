package co.edu.icesi.VirtualStore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.util.UUID;

@Data
@Table(name = "order_items")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType")
    @Column(name = "order_item_id")
    private UUID id;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @PrePersist
    public void generateId(){
        this.id = UUID.randomUUID();
    }
}