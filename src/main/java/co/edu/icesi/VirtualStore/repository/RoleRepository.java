package co.edu.icesi.VirtualStore.repository;

import co.edu.icesi.VirtualStore.model.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface RoleRepository extends CrudRepository<Role, UUID> {

    @Query(value = "SELECT role_id,name,description FROM roles WHERE role_id = 'c7cc9bab-62e1-4be1-98d3-8908a2c1784f'", nativeQuery = true)
    Role getBasicUserRole();
}