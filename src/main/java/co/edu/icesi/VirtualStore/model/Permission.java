package co.edu.icesi.VirtualStore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Table(name = "permissions")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType")
    @Column(name = "permission_id")
    private UUID id;

    private String uri;

    private String key;
}