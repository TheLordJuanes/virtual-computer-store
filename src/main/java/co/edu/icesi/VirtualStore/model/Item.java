package co.edu.icesi.VirtualStore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.util.UUID;

@Data
@Table(name = "items")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType")
    @Column(name = "item_id")
    private UUID id;

    private String name;

    private String description;

    private double price;

    @Column(name = "url_image")
    private String urlImage;

    @PrePersist
    public void generateId(){
        this.id = UUID.randomUUID();
    }
}