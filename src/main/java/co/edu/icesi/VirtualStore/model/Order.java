package co.edu.icesi.VirtualStore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.util.UUID;

@Data
@Table(name = "orders")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType")
    @Column(name = "order_id")
    private UUID id;

    private double total;

    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void generateId(){
        this.id = UUID.randomUUID();
    }
}