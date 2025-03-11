package com.pureGlow.pureGlow.Repositories;

import com.pureGlow.pureGlow.Entities.Client;
import com.pureGlow.pureGlow.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsBySales_Client(Client client);
    boolean existsByAppointments_Client(Client client);
    Optional<Client> findByUser(User user);
}
