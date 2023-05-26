package co.edu.icesi.VirtualStore.repository;

import co.edu.icesi.VirtualStore.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET role_id = '44c953c9-daf4-45a9-bb41-288fce256c43' WHERE user_id = ?1", nativeQuery = true)
    void updateUserToAdmin(UUID userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE users SET last_login = ?2 WHERE user_id = ?1", nativeQuery = true)
    void updateLastLogin(UUID userId, String lastLogin);
}
