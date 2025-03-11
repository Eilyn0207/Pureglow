package com.pureGlow.pureGlow.Repositories;

import com.pureGlow.pureGlow.Entities.Seller;
import com.pureGlow.pureGlow.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByUser(User user);
    boolean existsBySales_Seller(Seller seller);
}
