package com.pureGlow.pureGlow.Repositories;

import com.pureGlow.pureGlow.Entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
}
