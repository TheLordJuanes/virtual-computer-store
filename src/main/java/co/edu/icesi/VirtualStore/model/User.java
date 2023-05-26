package co.edu.icesi.VirtualStore.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Table(name = "users")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Type(type="org.hibernate.type.PostgresUUIDType")
    @Column(name = "user_id")
    private UUID id;

    private String email;

    @NotNull
    private String password;

    @NotNull
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "last_login")
    private String lastLogin;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @PrePersist
    public void generateId(){
        this.id = UUID.randomUUID();
    }
}
