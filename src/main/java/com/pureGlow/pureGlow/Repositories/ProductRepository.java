package com.pureGlow.pureGlow.Repositories;

import com.pureGlow.pureGlow.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsBySaleDetails_Product(Product product);
}
