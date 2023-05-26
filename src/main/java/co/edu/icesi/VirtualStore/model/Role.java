package co.edu.icesi.VirtualStore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@Table(name = "roles")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType")
    @Column(name = "role_id")
    private UUID id;

    @NotNull
    private String name;

    @NotNull
    private String description;
}
