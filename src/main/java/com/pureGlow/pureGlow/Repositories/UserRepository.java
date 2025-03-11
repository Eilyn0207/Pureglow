package com.pureGlow.pureGlow.Repositories;

import com.pureGlow.pureGlow.Entities.Client;
import com.pureGlow.pureGlow.Entities.Seller;
import com.pureGlow.pureGlow.Entities.Trainer;
import com.pureGlow.pureGlow.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByClient(Client client);
    Optional<User> findBySeller(Seller seller);
    Optional<User> findByTrainer(Trainer trainer);
    Optional<User> findByEmail(String email);
}
