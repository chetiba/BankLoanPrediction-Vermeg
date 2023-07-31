package com.vermeg.chtiba.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.vermeg.chtiba.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String paramString);

    boolean existsByEmail(String paramString);

    Optional<Client> findByToken(String paramString);

    @Query("SELECT u FROM Client u WHERE u.email = :email")
    Client findByEmail1(@Param("email") String paramString);
}
